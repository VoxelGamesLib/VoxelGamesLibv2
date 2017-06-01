package me.minidigger.voxelgameslib.event.events.game;

import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.game.Game;

/**
 * Called when a new game starts
 */
public class GameStartEvent extends GameEvent {

  /**
   * @param game the game that was started
   */
  public GameStartEvent(@Nonnull Game game) {
    super(game);
  }
}
