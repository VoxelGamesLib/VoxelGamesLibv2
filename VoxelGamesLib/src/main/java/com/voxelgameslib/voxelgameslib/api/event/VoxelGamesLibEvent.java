package com.voxelgameslib.voxelgameslib.api.event;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Marker class for events
 */
public abstract class VoxelGamesLibEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

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
