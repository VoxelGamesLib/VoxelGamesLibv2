package me.minidigger.voxelgameslib.utils;

import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Small util for chat related stuff
 */
public class ChatUtil {

  /**
   * Serialises a array of base components into a single string (by calling #toPlainText on them)
   *
   * @param comps the base components to serialize into text
   * @return the readable text
   */
  public static String toPlainText(@Nonnull BaseComponent... comps) {
    StringBuilder sb = new StringBuilder();
    for (BaseComponent comp : comps) {
      sb.append(comp.toPlainText());
    }
    return sb.toString();
  }
}
