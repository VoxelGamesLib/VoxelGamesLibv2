package me.minidigger.voxelgameslib.tick;

/**
 * A Tickable is a object that should be notified about a server tick. A server tick occurs every
 * 1/20 second (20 ticks per second)
 */
public interface Tickable {

    /**
     * called before the first tick
     */
    void start();

    /**
     * called when it stops ticking
     */
    void stop();

    /**
     * called on every tick
     */
    void tick();
}
