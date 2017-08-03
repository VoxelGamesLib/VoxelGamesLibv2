package com.voxelgameslib.voxelgameslib.event.events.game;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.user.User;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called when a user left a game
 */
public class GameLeaveEvent extends GameEvent {

    private static final HandlerList handlers = new HandlerList();
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
