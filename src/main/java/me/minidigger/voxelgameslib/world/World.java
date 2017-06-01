package me.minidigger.voxelgameslib.world;

import me.minidigger.voxelgameslib.ImplementMe;
import me.minidigger.voxelgameslib.block.Block;
import me.minidigger.voxelgameslib.map.Vector3D;

/**
 * Represents a world on the server
 */
public interface World<T> extends ImplementMe<T> {

  /**
   * @return The name of this world
   */
  String getName();

  /**
   * Gets the block at the specified location
   *
   * @param location the location
   * @return the block at that location
   */
  Block getBlockAt(Vector3D location);
}
