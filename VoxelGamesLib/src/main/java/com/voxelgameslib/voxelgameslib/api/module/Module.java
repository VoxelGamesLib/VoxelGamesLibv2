package com.voxelgameslib.voxelgameslib.api.module;

/**
 * A module as a extension to the framework. it could be a servermod implementation, a gamemode or just a general lib.
 */
public interface Module {

    /**
     * Called when this module is enabled
     */
    void enable();

    /**
     * Called when this module is disabled
     */
    void disable();
}
