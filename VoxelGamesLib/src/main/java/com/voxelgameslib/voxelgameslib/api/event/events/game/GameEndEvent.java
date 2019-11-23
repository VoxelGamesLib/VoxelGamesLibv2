package com.voxelgameslib.voxelgameslib.api.event.events.game;

import java.time.Duration;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.Game;
import com.voxelgameslib.voxelgameslib.components.user.User;

import org.bukkit.event.HandlerList;

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
    public GameEndEvent(@Nonnull Game game, @Nonnull List<User> winners, @Nonnull Duration duration,
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

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }
}
