package com.voxelgameslib.voxelgameslib.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Small annotation to add info to conditions that might be used in documentation
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VictoryConditionInfo {

    /**
     * @return the name of the condition
     */
    @Nonnull
    String name();

    /**
     * @return the author of the condition
     */
    @Nonnull
    String author();

    /**
     * @return the version of the condition
     */
    @Nonnull
    String version();

    /**
     * @return the description of the condition
     */
    @Nonnull
    String description();
}
