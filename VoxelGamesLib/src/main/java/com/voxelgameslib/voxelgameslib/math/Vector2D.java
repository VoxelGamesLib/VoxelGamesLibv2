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
package com.voxelgameslib.voxelgameslib.math;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an immutable vector in two dimensions. This class is not final by design.
 */
public class Vector2D implements Cloneable {

    @Expose
    private final double x;
    @Expose
    private final double y;

    protected Vector2D() {
        // JPA
        x = -1;
        y = -1;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Tries to generate a string from a vector. The string should be in the format defined by {@link
     * Vector3D#toString()}
     *
     * @param string The formatted string
     * @return A new vector, or null if the format is incorrect.
     */
    @Nullable
    public static Vector2D fromString(@Nonnull String string) {
        if (!string.startsWith("V") || !string.endsWith(";")) {
            return null;
        }
        String[] split = string.replace("V", "").replace(";", "").split(",");
        try {
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            return new Vector2D(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public final Vector2D clone() {
        return new Vector2D(x, y);
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

        Vector2D vector3D = (Vector2D) o;

        return Double.compare(vector3D.x, x) == 0 && Double.compare(vector3D.y, y) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public final String toString() {
        return "V" + x + "," + y + ";";
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    /**
     * Adds values to this vector
     *
     * @param x The amount to add to the x value
     * @param y The amount to add to the y value
     * @return A new vector
     */
    @Nonnull
    public final Vector2D add(double x, double y) {
        return new Vector2D(this.x + x, this.y + y);
    }

    /**
     * Performs vector addition
     *
     * @param vector The vector to add to this vector
     * @return A new vector
     */
    @Nonnull
    public final Vector2D add(@Nonnull Vector2D vector) {
        return new Vector2D(this.x + vector.x, this.y + vector.y);
    }

    /**
     * Performs scalar multiplication on this vector
     *
     * @param amount The scalar to multiply by
     * @return A new vector
     */
    @Nonnull
    public final Vector2D multiply(double amount) {
        return new Vector2D(x * amount, y * amount);
    }

    /**
     * Computes the dot product of two vectors
     *
     * @param other The other vector for which to compute the dot product
     * @return The dot product
     */
    public final double dotProduct(@Nonnull Vector2D other) {
        return (this.x * other.x) + (this.y * other.y);
    }

    /**
     * Computes the midpoint between two vectors
     *
     * @param other The other vector for which to compute the midpoint
     * @return A new vector representing the midpoint
     */
    @Nonnull
    public final Vector2D midpoint(@Nonnull Vector2D other) {
        return new Vector2D(
            (this.x + other.x) / 2,
            (this.y + other.y) / 2
        );
    }

    /**
     * Gets the magnitude of this vector
     *
     * @return A double representing the magnitude (length)
     */
    public final double length() {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * Gets the square of the magnitude of this vector. If at all possible, use this instead of {@link
     * Vector2D#length()} as this is significantly faster.
     *
     * @return A double representing the square of the magnitude
     */
    public final double lengthSquared() {
        return (x * x) + (y * y);
    }

    /**
     * Determines whether this vector is aligned to a block.
     *
     * @return True if it is.
     */
    public boolean isBlockAligned() {
        return (this.x == Math.floor(this.x) && this.y == Math.floor(this.y)
            && !Double.isInfinite(this.x) && !Double.isInfinite(this.y));
    }

    /**
     * Converts this vector into a location where z equals 0
     *
     * @param world the world the location is located in
     * @return the created location
     */
    @Nonnull
    public Location toLocation(@Nonnull String world) {
        return toLocation(world, 0);
    }

    /**
     * Converts this vector into a location
     *
     * @param world the world the location is located in
     * @param z     the location's z value
     * @return the created location
     */
    @Nonnull
    public Location toLocation(@Nonnull String world, double z) {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * Converts this vector into a three dimensional vector where z equals 0.
     *
     * @return This vector converted to a 3D one.
     */
    @Nonnull
    public Vector3D toVector3D() {
        return this.toVector3D(0);
    }


    /**
     * Converts this vector into a three dimensional vector.
     *
     * @param z The z value to be used by the new vector.
     * @return This vector converted to a 3D one.
     */
    @Nonnull
    public Vector3D toVector3D(double z) {
        return new Vector3D(x, y, z);
    }
}