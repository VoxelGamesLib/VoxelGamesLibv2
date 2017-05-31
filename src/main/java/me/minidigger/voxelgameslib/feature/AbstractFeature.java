package me.minidigger.voxelgameslib.feature;

import com.google.gson.annotations.Expose;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import me.minidigger.voxelgameslib.phase.Phase;

/**
 * Abstract implementation of Phase
 */
public abstract class AbstractFeature implements Feature {

  @Expose
  @Getter
  @Nonnull
  private String name;

  @Getter
  @Setter
  @Nonnull
  private Phase phase;

  /**
   * Sets the name of this feature to the name of the class
   */
  public AbstractFeature(@Nonnull Phase phase) {
    this.phase = phase;
    this.name = getClass().getName().replace(FeatureTypeAdapter.DEFAULT_PATH + ".", "");
  }
}
