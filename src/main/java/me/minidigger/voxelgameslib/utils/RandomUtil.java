package me.minidigger.voxelgameslib.utils;

import java.util.Random;

/**
 * Small util to work with random generated values
 */
public class RandomUtil {

  private static final long seed = 1337; //TODO test seed only, change for prod!

  private static Random random = new Random(seed);

  private static final char[] symbols;

  static {
    StringBuilder tmp = new StringBuilder();
    for (char ch = '0'; ch <= '9'; ++ch) {
      tmp.append(ch);
    }
    for (char ch = 'a'; ch <= 'z'; ++ch) {
      tmp.append(ch);
    }
    symbols = tmp.toString().toCharArray();
  }

  /**
   * @return the random instance that is currently in use
   */
  public static Random getRandom() {
    return random;
  }

  /**
   * Changes the current used random to a new instance with the given seed
   *
   * @param seed the seed of the new random
   */
  public static void setSeed(long seed) {
    random = new Random(seed);
  }

  /**
   * Generates a random string, length is random in range of 4-10.<br> Contains only 0-9 and a-z.
   *
   * @return the random string
   */
  public static String generateString() {
    return generateString(generateNumInRange(4, 10));
  }

  /**
   * Generates a random string with the given length.<br> Contains only 0-9 and a-z.
   *
   * @return the random string
   */
  public static String generateString(int length) {
    char[] buf = new char[length];
    for (int idx = 0; idx < buf.length; ++idx) {
      buf[idx] = symbols[random.nextInt(symbols.length)];
    }
    return new String(buf);
  }

  /**
   * Generates a random in from start (inclusive) to end (exclusive)
   *
   * @param start the min val (inclusive)
   * @param end the max val (exclusive)
   * @return the generated number
   */
  public static int generateNumInRange(int start, int end) {
    return start + random.nextInt(end - start);
  }

  /**
   * Returns a random entry of the given array
   *
   * @param array the array with all entries
   * @param <T> the type of the array
   * @return the random entry
   */
  public static <T> T randomEntry(T[] array) {
    return array[random.nextInt(array.length)];
  }
}
