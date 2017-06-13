package me.minidigger.voxelgameslib.event.events.game;

import javax.annotation.Nonnull;

import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.user.User;

/**
 * Called when a user joins a game
 */
public class GameJoinEvent extends GameEvent {

    private User user;

    /**
     * @param game the game the user joined
     * @param user the user that joined the game
     */
    public GameJoinEvent(@Nonnull Game game, @Nonnull User user) {
        super(game);
        this.user = user;
    }

    /**
     * @return the user that joined the game
     */
    @Nonnull
    public User getUser() {
        return user;
    }
}
