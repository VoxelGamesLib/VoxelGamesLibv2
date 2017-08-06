/*
 *  Copyright 2016 __0x277F <0x277F@gmail.com>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.voxelgameslib.voxelgameslib.map;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents an immutable vector in three dimensions. This class is not final by design.
 */
public class Vector3D implements Cloneable {

    public static final double EPSILON = 1e-6;

    @Expose
    private final double x;
    @Expose
    private final double y;
    @Expose
    private final double z;

    protected Vector3D() {
        // JPA
        x = -1;
        y = -1;
        z = -1;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public final Vector3D clone() {
        // We don't need to call super.clone() because the method is final.
        return new Vector3D(x, y, z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        // This uses a similar algorithm to Bukkit's because I can't be arsed to make my own.
        int a = 53;
        a *= (Double.doubleToRawLongBits(this.x) >> 32) ^ Double.doubleToRawLongBits(this.x);
        a *= (Double.doubleToRawLongBits(this.y) >> 32) ^ Double.doubleToRawLongBits(this.y);
        a *= (Double.doubleToRawLongBits(this.z) >> 32) ^ Double.doubleToRawLongBits(this.z);
        return a;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vector3D vector3D = (Vector3D) o;

        if (Double.compare(vector3D.x, x) != 0) {
            return false;
        }
        if (Double.compare(vector3D.y, y) != 0) {
            return false;
        }
        return Double.compare(vector3D.z, z) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public final String toString() {
        return "V" + x + "," + y + "," + z + ";";
    }

    /**
     * Tries to generate a string from a vector. The string should be in the format defined by
     * {@link Vector3D#toString()}
     *
     * @param string The formatted string
     * @return A new vector, or null if the format is incorrect.
     */
    @Nullable
    public static Vector3D fromString(@Nonnull String string) {
        if (!string.startsWith("V") || !string.endsWith(";")) {
            return null;
        }
        String[] split = string.replace("V", "").replace(";", "").split(",");
        try {
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);
            return new Vector3D(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getZ() {
        return z;
    }

    /**
     * Adds values to this vector
     *
     * @param x The amount to add to the x value
     * @param y The amount to add to the y value
     * @param z The amount to add to the z value
     * @return A new vector
     */
    @Nonnull
    public final Vector3D add(double x, double y, double z) {
        return new Vector3D(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Performs vector addition
     *
     * @param vector The vector to add to this vector
     * @return A new vector
     */
    @Nonnull
    public final Vector3D add(@Nonnull Vector3D vector) {
        return new Vector3D(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * Performs scalar multiplication on this vector
     *
     * @param amount The scalar to multiply by
     * @return A new vector
     */
    @Nonnull
    public final Vector3D multiply(double amount) {
        return new Vector3D(x * amount, y * amount, z * amount);
    }

    /**
     * Computes the cross product of two vectors
     *
     * @param other The other vector for which to compute the cross product
     * @return A new vector representing the cross product
     */
    @Nonnull
    public final Vector3D crossProduct(@Nonnull Vector3D other) {
        return new Vector3D(
                (this.y * other.z) - (this.z * other.y),
                (this.z * other.x) - (this.x * other.z),
                (this.x * other.y) - (this.y * other.x)
        );
    }

    /**
     * Computes the dot product of two vectors
     *
     * @param other The other vector for which to compute the dot product
     * @return The dot product
     */
    public final double dotProduct(@Nonnull Vector3D other) {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
    }

    /**
     * Computes the midpoint between two vectors
     *
     * @param other The other vector for which to compute the midpoint
     * @return A new vector representing the midpoint
     */
    @Nonnull
    public final Vector3D midpoint(@Nonnull Vector3D other) {
        return new Vector3D(
                (this.x + other.x) / 2,
                (this.y + other.y) / 2,
                (this.z + other.z) / 2
        );
    }

    /**
     * Rotates this vector around the Y axis in the given angle, in radians
     *
     * @param angle the angle in radians
     * @return a new, rotated vector
     */
    @Nonnull
    public final Vector3D rotateAroundY(double angle) {
        return new Vector3D(
                x * Math.cos(angle) - z * Math.sin(angle),
                y,
                x * Math.sin(angle) + z * Math.cos(angle)
        );
    }

    /**
     * Gets the magnitude of this vector
     *
     * @return A double representing the magnitude (length)
     */
    public final double length() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * Gets the square of the magnitude of this vector. If at all possible, use this instead of
     * {@link Vector3D#length()} as this is significantly faster.
     *
     * @return A double representing the square of the magnitude
     */
    public final double lengthSquared() {
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * Determines whether this vector is aligned to a block.
     *
     * @return True if it is.
     */
    public boolean isBlockAligned() {
        return (this.x == Math.floor(this.x) && this.y == Math.floor(this.y) && this.z == Math
                .floor(this.z)
                && !Double.isInfinite(this.x) && !Double.isInfinite(this.y) && !Double.isInfinite(this.z));
    }

    /**
     * Converts this vector into a location
     *
     * @param world the world the location is located in
     * @return the created location
     */
    @Nonnull
    public Location toLocation(@Nonnull String world) {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}