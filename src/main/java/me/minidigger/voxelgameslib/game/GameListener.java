package me.minidigger.voxelgameslib.game;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.event.events.game.GameLeaveEvent;
import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@Log
@SuppressWarnings("JavaDoc")
public class GameListener implements Listener {

  @Inject
  private GameHandler gameHandler;
  @Inject
  private UserHandler userHandler;

  @EventHandler
  public void onLeave(@Nonnull PlayerQuitEvent event) {
    User user = userHandler.getUser(event.getPlayer().getUniqueId())
        .orElseThrow(() -> new UserException(
            "Unknown user " + event.getPlayer().getDisplayName() + "(" + event.getPlayer()
                .getUniqueId() + ")"));
    gameHandler.getGames(event.getPlayer().getUniqueId(), true)
        .forEach((game -> game.leave(user)));
  }

  @EventHandler
  public void onL(@Nonnull GameLeaveEvent event) {
    log.finer(event.getUser().getDisplayName() + " left the game " + event.getGame()
        .getGameMode().getName());
  }

  @EventHandler
  public void onJ(@Nonnull GameJoinEvent event) {
    log.finer(event.getUser().getDisplayName() + " joined the game " + event.getGame()
        .getGameMode().getName());
  }
}
