package me.minidigger.voxelgameslib.event.events;

import me.minidigger.voxelgameslib.event.VoxelGamesLibEvent;

import org.bukkit.event.HandlerList;

/**
 * Called when this plugin was enabled
 */
public class VoxelGameLibEnableEvent extends VoxelGamesLibEvent {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
