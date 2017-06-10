package me.minidigger.voxelgameslib.utils;

import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.user.GamePlayer;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializer;
import org.bukkit.Bukkit;

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

  /**
   * Sends the message to the user
   *
   * @param gameUser the user that should get the message
   * @param message the message to send to the user
   */
  public static void sendMessage(GamePlayer gameUser, Component message) {
    String json = ComponentSerializer.serialize(message);
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + gameUser.getPlayer().getName() + " " + json);
  }
}
