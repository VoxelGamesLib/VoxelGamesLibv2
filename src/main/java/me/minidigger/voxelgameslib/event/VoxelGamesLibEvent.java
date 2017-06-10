package me.minidigger.voxelgameslib.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Marker class for events
 */
public class VoxelGamesLibEvent extends Event {

  private static final HandlerList handlers = new HandlerList();

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
