package com.voxelgameslib.voxelgameslib.map;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

/**
 * A marker marks a special position in the world. they are most of the time implemented by tile entities so that the
 * scanning process is easier/faster.
 */
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
    public Marker(@Nonnull Vector3D loc, double yaw, @Nonnull String data, @Nonnull MarkerDefinition markerDefinition) {
        this.loc = loc;
        this.yaw = yaw;
        this.data = data;
        this.markerDefinition = markerDefinition;
    }

    @Nonnull
    public Vector3D getLoc() {
        return this.loc;
    }

    public double getYaw() {
        return this.yaw;
    }

    @Nonnull
    public String getData() {
        return this.data;
    }

    public MarkerDefinition getMarkerDefinition() {
        return this.markerDefinition;
    }

    public void setMarkerDefinition(MarkerDefinition markerDefinition) {
        this.markerDefinition = markerDefinition;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Marker)) return false;
        final Marker other = (Marker) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$loc = this.getLoc();
        final Object other$loc = other.getLoc();
        if (this$loc == null ? other$loc != null : !this$loc.equals(other$loc)) return false;
        if (Double.compare(this.getYaw(), other.getYaw()) != 0) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        final Object this$markerDefinition = this.getMarkerDefinition();
        final Object other$markerDefinition = other.getMarkerDefinition();
        if (this$markerDefinition == null ? other$markerDefinition != null : !this$markerDefinition.equals(other$markerDefinition))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $loc = this.getLoc();
        result = result * PRIME + ($loc == null ? 43 : $loc.hashCode());
        final long $yaw = Double.doubleToLongBits(this.getYaw());
        result = result * PRIME + (int) ($yaw >>> 32 ^ $yaw);
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        final Object $markerDefinition = this.getMarkerDefinition();
        result = result * PRIME + ($markerDefinition == null ? 43 : $markerDefinition.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Marker;
    }

    public String toString() {
        return "Marker(loc=" + this.getLoc() + ", yaw=" + this.getYaw() + ", data=" + this.getData() + ", markerDefinition=" + this.getMarkerDefinition() + ")";
    }
}
