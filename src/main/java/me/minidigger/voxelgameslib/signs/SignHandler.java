package me.minidigger.voxelgameslib.signs;

import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.timings.Timings;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.extern.java.Log;

/**
 * Handles placeholder sign and interaction signs
 */
@Log
@Singleton
public class SignHandler implements Handler {

    @Inject
    private PersistenceHandler persistenceHandler;
    @Inject
    @Nullable
    private GlobalConfig config;
    @Inject
    private Injector injector;
    @Inject
    private VoxelGamesLib voxelGamesLib;

    private List<SignLocation> signLocations;
    private List<SignLocation> markedForRemoval;

    private boolean dirty = false;

    @Inject
    private SignButtons signButtons;
    @Inject
    private SignPlaceholders signPlaceholders;

    @Override
    public void start() {
        signButtons.registerButtons();

        signPlaceholders.init();

        markedForRemoval = new ArrayList<>();
        config = injector.getInstance(GlobalConfig.class);

        //TODO sign persistence
        // signLocations = persistenceHandler.getProvider().loadSigns();
        signLocations = new ArrayList<>();




        startUpdateTask();
    }

    @Override
    public void stop() {

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
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    updateSigns();
                } catch (Exception ex) {
                    log.warning("Error while updating signs, trying again...");
                    ex.printStackTrace();
                    startUpdateTask();
                }

                if (dirty) {
                    //TODO sign persistence
                    //persistenceHandler.getProvider().saveSigns(signLocations);
                    if (markedForRemoval != null && markedForRemoval.size() > 0) {
                        //persistenceHandler.getProvider().deleteSigns(markedForRemoval);
                        markedForRemoval.clear();
                    }
                    dirty = false;
                }
            }
        }.runTaskTimer(voxelGamesLib, config.signUpdateInterval * 20, config.signUpdateInterval * 20);
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
     * @param lines    the lines the new sign has
     */
    public void addSign(Location location, String[] lines) {
        dirty = true;
        if (!getSignAt(location).isPresent()) {
            signLocations.add(new SignLocation(location, lines));
        }
    }

    public SignPlaceholders getSignPlaceholders() {
        return signPlaceholders;
    }

    public SignButtons getSignButtons() {
        return signButtons;
    }
}
