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
  private int duration;
  private boolean wasAborted;

  /**
   * @param game the game that ended
   * @param winners the winners. can be 0, 1 or many
   * @param wasAborted if the game was aborted (server shutdown, all players leave, by command)
   */
  public GameEndEvent(@Nonnull Game game, @Nonnull List<User> winners, int duration, boolean wasAborted) {
    super(game);
    this.winners = winners;
    this.duration = duration;
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
   * @return duration of game (in seconds)
   */
  @Nonnull
  public int getGameDuration() {
    return duration;
  }

  /**
   * @return if the game was aborted (server shutdown, all players leave, or by command)
   */
  public boolean wasAborted() {
    return wasAborted;
  }
}
