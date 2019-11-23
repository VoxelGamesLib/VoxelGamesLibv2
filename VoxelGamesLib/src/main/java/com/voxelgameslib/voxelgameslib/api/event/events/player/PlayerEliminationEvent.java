package com.voxelgameslib.voxelgameslib.api.event.events.player;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.Game;
import com.voxelgameslib.voxelgameslib.components.user.User;

import org.bukkit.event.HandlerList;

/**
 * This event is called when a player is eliminated from a game.<br>Gametypes should call this themselves.
 */
public class PlayerEliminationEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Game game;

    public PlayerEliminationEvent(@Nonnull User user, @Nonnull Game game) {
        super(user);
        this.game = game;
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

    public Game getGame() {
        return this.game;
    }
}
