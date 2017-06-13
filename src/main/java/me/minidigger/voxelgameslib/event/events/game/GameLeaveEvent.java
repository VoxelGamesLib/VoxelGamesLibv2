package me.minidigger.voxelgameslib.event.events.game;

import javax.annotation.Nonnull;

import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.user.User;

/**
 * Called when a user left a game
 */
public class GameLeaveEvent extends GameEvent {

    private User user;

    /**
     * @param game the game the user left
     * @param user the user that left the game
     */
    public GameLeaveEvent(@Nonnull Game game, @Nonnull User user) {
        super(game);
        this.user = user;
    }

    /**
     * @return the user that left the game
     */
    @Nonnull
    public User getUser() {
        return user;
    }
}
