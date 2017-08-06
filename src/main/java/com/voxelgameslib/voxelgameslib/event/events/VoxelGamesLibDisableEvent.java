package com.voxelgameslib.voxelgameslib.event.events;

import com.voxelgameslib.voxelgameslib.event.VoxelGamesLibEvent;

import javax.annotation.Nonnull;

import org.bukkit.event.HandlerList;

/**
 * Called when this plugin was disabled
 */
public class VoxelGamesLibDisableEvent extends VoxelGamesLibEvent {

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
