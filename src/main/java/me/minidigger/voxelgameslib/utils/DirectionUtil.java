package me.minidigger.voxelgameslib.utils;

import java.util.EnumMap;
import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.block.Direction;

/**
 * Small util for direction and yaw conversation <br> Modified version of
 * https://github.com/bergerkiller/BKCommonLib/blob/master/src/main/java/com/bergerkiller/bukkit/common/utils/FaceUtil.java
 */
public class DirectionUtil {

  private static final Direction[] AXIS = new Direction[4];
  private static final Direction[] RADIAL = {Direction.WEST, Direction.NORTH_WEST, Direction.NORTH,
      Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH,
      Direction.SOUTH_WEST};
  private static final EnumMap<Direction, Integer> notches = new EnumMap<>(Direction.class);

  static {
    for (int i = 0; i < RADIAL.length; i++) {
      notches.put(RADIAL[i], i);
    }
    for (int i = 0; i < AXIS.length; i++) {
      AXIS[i] = RADIAL[i << 1];
    }
  }

  /**
   * Gets the Notch integer representation of a Direction<br>
   * <b>These are the horizontal directions, which exclude up and down</b>
   *
   * @param face to get
   * @return Notch of the face
   */
  public static int directionToNotch(@Nonnull Direction face) {
    Integer notch = notches.get(face);
    return notch == null ? 0 : notch;
  }

  /**
   * Gets the angle from a horizontal direction
   *
   * @param face to get the angle for
   * @return face angle
   */
  public static int directionToYaw(@Nonnull Direction face) {
    return MathUtil.wrapAngle(45 * directionToNotch(face));
  }

  /**
   * Gets the horizontal directions from a given yaw angle<br>
   * This includes the NORTH_WEST directions
   *
   * @param yaw angle
   * @return The direction of the angle
   */
  @Nonnull
  public static Direction yawToDirection(float yaw) {
    return yawToDirection(yaw, true);
  }

  /**
   * Gets the horizontal direction from a given yaw angle
   *
   * @param yaw angle
   * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be returned
   * @return The direction of the angle
   */
  @Nonnull
  public static Direction yawToDirection(float yaw, boolean useSubCardinalDirections) {
    if (useSubCardinalDirections) {
      return RADIAL[(Math.round((yaw - 90) / 45f) & 0x7)];
    } else {
      return AXIS[Math.round(yaw / 90f) & 0x3];
    }
  }

  /**
   * Gets the next direction clock wise
   *
   * @param direction the starting direction
   * @param steps the amount of steps to go
   * @return the direction
   */
  public static Direction getNext(Direction direction, int steps) {
    for (int i = 0; i < RADIAL.length; i++) {
      if (RADIAL[i] == direction) {
        return RADIAL[Math.floorMod(i + steps, RADIAL.length)];
      }
    }
    return Direction.SELF;
  }
}
