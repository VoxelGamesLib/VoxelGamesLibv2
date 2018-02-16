package com.voxelgameslib.voxelgameslib.handler;

/**
 * A {@link Handler} is an object that needs to handle certain types of things. It will be created and destroyed by the
 * main class.
 */
public interface Handler {

    /**
     * Called on server enable
     */
    void enable();

    /**
     * Called on server disable. used to cleanup things.
     */
    void disable();
}
