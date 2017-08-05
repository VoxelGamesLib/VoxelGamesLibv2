package com.voxelgameslib.voxelgameslib.map;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

import lombok.Data;

/**
 * A marker marks a special position in the world. they are most of the time implemented by tile
 * entities so that the scanning process is easier/faster.
 */
@Data
public class Marker {

    @Nonnull
    @Expose
    private final Vector3D loc;
    @Expose
    private final double yaw;
    @Nonnull
    @Expose
    private final String data;

    private MarkerDefinition markerDefinition;

    /**
     * Constructs a new marker
     *
     * @param loc  the location that this marker marks
     * @param yaw  the yaw for this marker
     * @param data the data of this marker
     */
    public Marker(@Nonnull Vector3D loc, double yaw, @Nonnull String data, MarkerDefinition markerDefinition) {
        this.loc = loc;
        this.yaw = yaw;
        this.data = data;
        this.markerDefinition = markerDefinition;
    }
}
