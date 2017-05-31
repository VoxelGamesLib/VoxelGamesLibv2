package me.minidigger.voxelgameslib.exception;

import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.phase.Phase;

/**
 * Thrown when something tries to access a {@link Feature} in a {@link Phase} that is not present at
 * this time.
 */
public class NoSuchFeatureException extends VoxelGameLibException {

  /**
   * @param clazz the class of the feature that was tried to access
   */
  public NoSuchFeatureException(@Nonnull Class<? extends Feature> clazz) {
    super("Could not find feature " + clazz.getSimpleName());
  }
}
