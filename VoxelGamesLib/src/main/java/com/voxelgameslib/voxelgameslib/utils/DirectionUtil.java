package com.voxelgameslib.voxelgameslib.utils;

import java.util.EnumMap;
import javax.annotation.Nonnull;

import org.bukkit.block.BlockFace;

/**
 * Small util for direction and yaw conversation <br> Modified version of https://github.com/bergerkiller/BKCommonLib/blob/master/src/main/java/com/bergerkiller/bukkit/common/utils/FaceUtil.java
 */
public class DirectionUtil {

    private static final BlockFace[] AXIS = new BlockFace[4];
    private static final BlockFace[] RADIAL = {BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH,
            BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH,
            BlockFace.SOUTH_WEST};
    private static final EnumMap<BlockFace, Integer> notches = new EnumMap<>(BlockFace.class);

    static {
        for (int i = 0; i < RADIAL.length; i++) {
            notches.put(RADIAL[i], i);
        }
        for (int i = 0; i < AXIS.length; i++) {
            AXIS[i] = RADIAL[i << 1];
        }
    }

    /**
     * Gets the Notch integer representation of a BlockFace<br> <b>These are the horizontal directions, which exclude up
     * and down</b>
     *
     * @param face to get
     * @return Notch of the face
     */
    public static int directionToNotch(@Nonnull BlockFace face) {
        Integer notch = notches.get(face);
        return notch == null ? 0 : notch;
    }

    /**
     * Gets the angle from a horizontal BlockFace
     *
     * @param face to get the angle for
     * @return face angle
     */
    public static int directionToYaw(@Nonnull BlockFace face) {
        return MathUtil.wrapAngle(45 * directionToNotch(face));
    }

    /**
     * Gets the horizontal BlockFace from a given yaw angle<br> This includes the NORTH_WEST directions
     *
     * @param yaw angle
     * @return The direction of the angle
     */
    @Nonnull
    public static BlockFace yawToDirection(float yaw) {
        return yawToDirection(yaw, true);
    }

    /**
     * Gets the horizontal BlockFace from a given yaw angle
     *
     * @param yaw                      angle
     * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be returned
     * @return The direction of the angle
     */
    @Nonnull
    public static BlockFace yawToDirection(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return RADIAL[(Math.round((yaw - 90) / 45f) & 0x7)];
        } else {
            return AXIS[Math.round(yaw / 90f) & 0x3];
        }
    }

    /**
     * Gets the next BlockFace clock wise
     *
     * @param direction the starting direction
     * @param steps     the amount of steps to go
     * @return the direction
     */
    @Nonnull
    public static BlockFace getNext(@Nonnull BlockFace direction, int steps) {
        for (int i = 0; i < RADIAL.length; i++) {
            if (RADIAL[i] == direction) {
                return RADIAL[Math.floorMod(i + steps, RADIAL.length)];
            }
        }
        return BlockFace.SELF;
    }
}
