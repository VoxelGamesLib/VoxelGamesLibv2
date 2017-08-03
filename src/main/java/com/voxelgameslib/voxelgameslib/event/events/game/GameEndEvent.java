package com.voxelgameslib.voxelgameslib.event.events.game;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.user.User;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.List;

/**
 * Called when a game is done.
 */
public class GameEndEvent extends GameEvent {

    private static final HandlerList handlers = new HandlerList();
    private List<User> winners;
    private Duration duration;
    private boolean wasAborted;

    /**
     * @param game       the game that ended
     * @param winners    the winners. can be 0, 1 or many
     * @param duration   the duration the game run for
     * @param wasAborted if the game was aborted (server shutdown, all players leave, by command)
     */
    public GameEndEvent(@Nonnull Game game, @Nonnull List<User> winners, Duration duration,
                        boolean wasAborted) {
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
     * @return duration of game
     */
    @Nonnull
    public Duration getGameDuration() {
        return duration;
    }

    /**
     * @return if the game was aborted (server shutdown, all players leave, or by command)
     */
    public boolean wasAborted() {
        return wasAborted;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
