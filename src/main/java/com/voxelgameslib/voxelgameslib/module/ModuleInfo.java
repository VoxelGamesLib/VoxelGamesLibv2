package com.voxelgameslib.voxelgameslib.module;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as module, specifies some useful data about the module
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

    /**
     * @return the name of the module
     */
    @Nonnull String name();

    /**
     * @return the authors of the module
     */
    @Nonnull String[] authors();

    /**
     * @return the version of the module
     */
    @Nonnull String version();
}
