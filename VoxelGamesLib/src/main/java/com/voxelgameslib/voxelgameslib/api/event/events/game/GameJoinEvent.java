package com.voxelgameslib.voxelgameslib.api.event.events.game;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.Game;
import com.voxelgameslib.voxelgameslib.components.user.User;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a user joins a game
 */
public class GameJoinEvent extends GameEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private User user;
    private boolean cancelled;

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

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
