package com.voxelgameslib.voxelgameslib.event.events.game;

import com.voxelgameslib.voxelgameslib.game.Game;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called when a new game starts
 */
public class GameStartEvent extends GameEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * @param game the game that was started
     */
    public GameStartEvent(@Nonnull Game game) {
        super(game);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
