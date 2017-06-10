package me.minidigger.voxelgameslib.user;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.utils.MojangUtil;
import org.bukkit.entity.Player;

@Log
@Singleton
public class UserHandler implements Handler {

  @Inject
  private GameHandler gameHandler;
  @Inject
  private PersistenceHandler persistenceHandler;
  @Inject
  private Injector injector;

  private Map<UUID, User> users;
  private Map<UUID, User> tempData;

  @Override
  public void start() {
    users = new HashMap<>();
    tempData = new ConcurrentHashMap<>();
  }

  @Override
  public void stop() {
    users.clear();
    tempData.clear();
  }

  /**
   * Creates the user object for a new user
   *
   * @param player the player that joined
   * @throws UserException when the player was not logged in
   */
  public void join(@Nonnull Player player) {
    if (!hasLoggedIn(player.getUniqueId())) {
      throw new UserException("User " + player.getName() + "(" + player.getUniqueId()
          + ") tried to join without being logged in!");
    }

    User user = tempData.remove(player.getUniqueId());
    user.setPlayer(player);
    users.put(user.getUuid(), user);
    log.info(
        "Applied data for user " + user.getUuid() + "(" + user.getRole().getName() + " "
            + user.getRawDisplayName() + ")");
  }

  /**
   * Handles logout.
   *
   * @param id the uuid of the user that logged out
   */
  public void logout(@Nonnull UUID id) {
    Optional<User> user = getUser(id);
    if (user.isPresent()) {
      //TODO go away, gamehandler, use the events!
      gameHandler.getGames(user.get().getUuid(), true).forEach(game -> game.leave(user.get()));
      persistenceHandler.getProvider().saveUser(user.get());
    }

    users.remove(id);
    tempData.remove(id);
  }

  /**
   * searches for a user with that uuid
   *
   * @param id the uuid of the user
   * @return the user with that uuid, if present
   */
  @Nonnull
  public Optional<User> getUser(@Nonnull UUID id) {
    return Optional.ofNullable(users.get(id));
  }

  /**
   * Called when a user logs in. used to load all kind of stuff. Should only be called async!
   *
   * @param uniqueId the id of the user that logged in
   * @return false if the user needs to be kicked
   */
  public boolean login(@Nonnull UUID uniqueId) {
    log.info("Loading data for user " + uniqueId);

    Optional<User> data = persistenceHandler.getProvider().loadUser(uniqueId);
    if (data.isPresent()) {
      User userDara = data.get();
      injector.injectMembers(userDara);
      tempData.put(uniqueId, userDara);
    } else {
      User user = new GamePlayer();
      user.setUuid(uniqueId);
      try {
        user.setDisplayName(MojangUtil.getDisplayName(uniqueId));
      } catch (Exception ignore) {
        // offline users don't have a real name
      }
      injector.injectMembers(user);
      tempData.put(uniqueId, user);
    }

    return true;
  }

  /**
   * Checks if a user has logged in (if the data was loaded successfully)
   *
   * @param uniqueId the id of the user that should be checked
   * @return true if everything is good, false if the data was not loaded
   */
  public boolean hasLoggedIn(@Nonnull UUID uniqueId) {
    return tempData.containsKey(uniqueId);
  }

  /**
   * searches for a user with that display name
   *
   * @param displayname the display name of the user
   * @return the user with that display name, if present
   */
  public Optional<User> getUser(String displayname) {
    if (displayname.equalsIgnoreCase("CONSOLE")) {
      return Optional.of(GameConsoleUser.INSTANCE);
    } else {
      return users.values().stream()
          .filter(u -> u.getPlayer().getName().equalsIgnoreCase(displayname)).findFirst();
    }
  }

  /**
   * @return all users currently online
   */
  public Collection<User> getUsers() {
    return users.values();
  }
}
