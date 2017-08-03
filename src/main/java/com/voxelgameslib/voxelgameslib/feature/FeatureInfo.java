package com.voxelgameslib.voxelgameslib.feature;

import java.lang.annotation.*;

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
