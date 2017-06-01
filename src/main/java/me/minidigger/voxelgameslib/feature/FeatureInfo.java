package me.minidigger.voxelgameslib.feature;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Small annotation to add info to features that might be used in documentation
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureInfo {

  /**
   * @return the name of the feature
   */
  String name();

  /**
   * @return the author of the feature
   */
  String author();

  /**
   * @return the version of the feature
   */
  String version();

  /**
   * @return the description of the feature
   */
  String description();
}
