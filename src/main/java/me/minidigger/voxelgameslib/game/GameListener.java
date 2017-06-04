package me.minidigger.voxelgameslib.game;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.event.events.game.GameLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@Log
@SuppressWarnings("JavaDoc")
public class GameListener implements Listener{

  @Inject
  private GameHandler gameHandler;

  @EventHandler
  public void onLeave(@Nonnull PlayerQuitEvent event) {
    gameHandler.getGames(event.getUser(), true).forEach((game -> game.leave(event.getPlayer())));
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
