package me.minidigger.voxelgameslib.game;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.event.events.game.GameLeaveEvent;
import me.minidigger.voxelgameslib.event.events.user.UserLeaveEvent;

@Log
@SuppressWarnings("JavaDoc")
public class GameListener {

  @Inject
  private GameHandler gameHandler;
  @Inject
  private VGLEventHandler eventHandler;

  public void onLeave(@Nonnull UserLeaveEvent event) {
    gameHandler.getGames(event.getUser(), true).forEach((game -> game.leave(event.getUser())));
  }

  public void onL(@Nonnull GameLeaveEvent event) {
    log.finer(event.getUser().getData().getDisplayName() + " left the game " + event.getGame()
        .getGameMode().getName());
  }

  public void onJ(@Nonnull GameJoinEvent event) {
    log.finer(event.getUser().getData().getDisplayName() + " joined the game " + event.getGame()
        .getGameMode().getName());
  }
}
