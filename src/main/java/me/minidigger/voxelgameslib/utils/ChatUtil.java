package me.minidigger.voxelgameslib.utils;

import javax.annotation.Nonnull;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

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
  public static String toPlainText(@Nonnull Component... comps) {
    StringBuilder sb = new StringBuilder();
    for (Component comp : comps) {
      toPlainText(sb, comp);
    }
    return sb.toString();
  }

  private static void toPlainText(StringBuilder sb, Component component) {
    if (component instanceof TextComponent) {
      sb.append(((TextComponent) component).content());
    }
    if (component.children().size() > 0) {
      component.children().forEach(c -> toPlainText(sb, c));
    }
  }
}
