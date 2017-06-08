package me.minidigger.voxelgameslib.signs;

import com.google.inject.Injector;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.timings.Timings;
import net.kyori.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Handles placeholder sign and interaction signs
 */
@Log
@Singleton
public class SignHandler implements Handler {

  @Inject
  private PersistenceHandler persistenceHandler;
  @Inject
  private GlobalConfig config;
  @Inject
  private Injector injector;

  private Map<String, SignButton> buttons;
  private Map<String, SignPlaceHolder> placeHolders;
  private List<SignLocation> signLocations;
  private List<SignLocation> markedForRemoval;

  private boolean dirty = false;

  @Override
  public void start() {
    placeHolders = new HashMap<>();
    markedForRemoval = new ArrayList<>();
    buttons = new HashMap<>();
    config = injector.getInstance(GlobalConfig.class);

    signLocations = persistenceHandler.getProvider().loadSigns();

    placeHolders.put("world", (SimpleSignPlaceHolder) (event, key) -> event.getWorld());
    placeHolders.put("time",
        (SimpleSignPlaceHolder) (event, key) -> DateTimeFormatter.ISO_TIME.format(LocalTime.now()));
    placeHolders.put("location",
        (SimpleSignPlaceHolder) (event, key) -> event.getLocation().toString().replace("V", ""));

    buttons.put("test", (user, block) -> user.sendMessage(new TextComponent("WOW")));

    startUpdateTask();
  }

  @Override
  public void stop() {
    placeHolders.clear();
    placeHolders = null;
  }

  /**
   * gets map with all registered sign placeholders
   *
   * @return all sign placeholders
   */
  public Map<String, SignPlaceHolder> getPlaceHolders() {
    return placeHolders;
  }

  /**
   * gets map with all registered sign buttons
   *
   * @return all sign buttons
   */
  public Map<String, SignButton> getButtons() {
    return buttons;
  }

  /**
   * Checks if there is a sign at the given location and returns it
   *
   * @param location the location to check
   * @return the sign, if present
   */
  public Optional<SignLocation> getSignAt(Location location) {
    for (SignLocation signLocation : signLocations) {
      if (signLocation.getLocation().equals(location)) {
        return Optional.of(signLocation);
      }
    }
    return Optional.empty();
  }

  /**
   * Starts the task to update signs
   */
  public void startUpdateTask() {
    VoxelGamesLib.newChain().delay(config.signUpdateInterval, TimeUnit.SECONDS)
        .sync(this::updateSigns).execute(this::startUpdateTask, (e, t) -> {
      log.warning("Error while updating signs, trying again...");
      e.printStackTrace();
      startUpdateTask();
    });

    if (dirty) {
      persistenceHandler.getProvider().saveSigns(signLocations);
      if (markedForRemoval != null && markedForRemoval.size() > 0) {
        persistenceHandler.getProvider().deleteSigns(markedForRemoval);
        markedForRemoval.clear();
      }
      dirty = false;
    }
  }

  private void updateSigns() {
    Timings.time("UpdateSigns", () -> {
      Iterator<SignLocation> iterator = signLocations.listIterator();
      while (iterator.hasNext()) {
        SignLocation loc = iterator.next();
        if (loc.isStillValid()) {
          loc.fireUpdateEvent();
        } else {
          log.finer("Removing old placeholder sign at " + loc.getLocation());
          dirty = true;
          iterator.remove();
          markedForRemoval.add(loc);
        }
      }
    });
  }

  /**
   * Removes a sign from the sign location list
   *
   * @param block the block to remove
   */
  public void removeSign(Block block) {
    dirty = true;
    ListIterator<SignLocation> iterator = signLocations.listIterator();
    while (iterator.hasNext()) {
      SignLocation loc = iterator.next();
      if (loc.getLocation().equals(block.getLocation())) {
        iterator.remove();
        markedForRemoval.add(loc);
      }
    }
  }

  /**
   * Adds a entry to the sign location list
   *
   * @param location the location of the new sign
   * @param lines the lines the new sign has
   */
  public void addSign(Location location, String[] lines) {
    dirty = true;
    if (!getSignAt(location).isPresent()) {
      signLocations.add(new SignLocation(location, lines));
    }
  }
}
