package me.minidigger.voxelgameslib.event.events.game;

import java.util.List;
import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.user.User;

/**
 * Called when a game is done.
 */
public class GameEndEvent extends GameEvent {

  private List<User> winners;
  private boolean wasAborted;

  /**
   * @param game the game that ended
   * @param winners the winners. can be 0, 1 or many
   * @param wasAborted if the game was aborted (server shutdown, all players leave, by command)
   */
  public GameEndEvent(@Nonnull Game game, @Nonnull List<User> winners, boolean wasAborted) {
    super(game);
    this.winners = winners;
    this.wasAborted = wasAborted;
  }

  /**
   * @return the winners. can be 0, 1 or many
   */
  @Nonnull
  public List<User> getWinners() {
    return winners;
  }

  /**
   * @return if the game was aborted (server shutdown, all players leave, by command)
   */
  public boolean wasAborted() {
    return wasAborted;
  }
}
