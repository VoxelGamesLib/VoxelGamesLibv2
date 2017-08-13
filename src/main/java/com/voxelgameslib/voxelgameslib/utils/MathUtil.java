package com.voxelgameslib.voxelgameslib.utils;

/**
 * Util for math related stuff
 */
public class MathUtil {

    /**
     * calculates and returns the inverse of a modulo n, both of which should be positive.  If the inverse does not
     * exist, 0 is returned.
     *
     * @param a a
     * @param n n
     * @return modInverse
     */
    public static int modInverse(int a, int n) {
        int i = n, v = 0, d = 1;
        while (a > 0) {
            int t = i / a, x = a;
            a = i % x;
            i = x;
            x = d;
            d = v - t * x;
            v = x;
        }
        v %= n;
        if (v < 0) {
            v = (v + n) % n;
        }
        return v;
    }

    /**
     * Wraps the angle to be between -180 and 180 degrees
     *
     * @param angle to wrap
     * @return a value between -180 and 180 degrees
     */
    public static int wrapAngle(int angle) {
        int wrappedAngle = angle;
        while (wrappedAngle <= -180) {
            wrappedAngle += 360;
        }
        while (wrappedAngle > 180) {
            wrappedAngle -= 360;
        }
        return wrappedAngle;
    }
}
