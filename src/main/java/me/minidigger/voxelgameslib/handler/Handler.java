package me.minidigger.voxelgameslib.handler;

/**
 * A {@link Handler} is an object that needs to handle certain types of things. It will be created
 * and destroyed by the main class.
 */
public interface Handler {

    /**
     * Called on server start
     */
    void start();

    /**
     * Called on server stop. used to cleanup things.
     */
    void stop();
}
