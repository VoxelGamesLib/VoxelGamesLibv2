package com.voxelgameslib.voxelgameslib.components.points;

/**
 * Defines the logic all points must implement.
 * <p>
 * Generally, you can just use the {@link GamePoint} class for most use cases,
 * but you can also implement Point and make something more powerful if you wish
 */
public interface Point {
    /**
     * The name of the Point
     *
     * @return name
     */
    String getName();

    /**
     * Set the name of the point
     *
     * @param name the new name
     */
    void setName(String name);

    /**
     * Should the point be persisted for users
     *
     * @return persisted
     */
    boolean isPersist();

    /**
     * Set whether the point should be persisted for users
     */
    void setPersist(boolean persist);
}
