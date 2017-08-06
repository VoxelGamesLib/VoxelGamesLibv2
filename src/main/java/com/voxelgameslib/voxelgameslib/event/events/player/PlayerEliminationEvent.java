package com.voxelgameslib.voxelgameslib.event.events.player;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.user.User;

import javax.annotation.Nonnull;

import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * This event is called when a player is eliminated from a game.<br>Gametypes should call this
 * themselves.
 */
public class PlayerEliminationEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    @Getter
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
}
