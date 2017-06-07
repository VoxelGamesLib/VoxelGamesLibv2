package me.minidigger.voxelgameslib.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import me.minidigger.voxelgameslib.exception.LangException;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import me.minidigger.voxelgameslib.utils.ChatUtil;
import net.kyori.text.BaseComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Gives quick access to the lang storage and translation and stuff
 */
public class Lang {

  @Inject
  private static LangHandler handler;
  @Inject
  private static UserHandler userHandler;

  /**
   * Creates an ComponentBuilder based on that LangKey
   *
   * @param key the lang key that should be translated
   * @return the created component builder
   */
  @Nonnull
  public static TextComponent trans(@Nonnull LangKey key) {
    return trans(key, handler.getDefaultLocale());
  }

  /**
   * Creates an ComponentBuilder based on that LangKey<br> The specified arguments are used to fill
   * out placeholders
   *
   * @param key the lang key that should be translated
   * @param args the arguments that should be replaying placeholders
   * @return the created component builder
   */
  @Nonnull
  public static TextComponent trans(@Nonnull LangKey key, @Nullable Object... args) {
    return trans(key, handler.getDefaultLocale(), args);
  }

  /**
   * Creates an ComponentBuilder based on that LangKey<br> Allows to specify a locale that should be
   * used to translate
   *
   * @param key the lang key that should be translated
   * @param loc the locale that should be used to translate the key
   * @return the created component builder
   */
  @Nonnull
  public static TextComponent trans(@Nonnull LangKey key, @Nonnull Locale loc) {
    return trans(key, loc, new Object[0]);
  }

  /**
   * Creates an ComponentBuilder based on that LangKey<br> Allows to specify a locale that should be
   * used to translate<br> The specified arguments are used to fill out placeholders
   *
   * @param key the lang key that should be translated
   * @param loc the locale that should be used to translate the key
   * @param args the arguments that should be replacing placeholders
   * @return the created component builder
   */
  @Nonnull
  public static TextComponent trans(@Nonnull LangKey key, @Nonnull Locale loc,
      @Nullable Object... args) {
    if (args == null) {
      args = new Object[0];
    }

    return parseFormat(string(key, loc, args));
  }

  /**
   * Parses a string into a component builder.<br> Takes care of {color} variables
   *
   * @param string the input string
   * @return the outputted and properly filled component builder
   */
  @Nonnull
  public static TextComponent parseFormat(@Nonnull String string) {
    TextComponent componentBuilder = new TextComponent("");
    String[] tokens = string.split("\\{|}");
    TextColor savedColor = TextColor.WHITE;
    outer:
    for (String token : tokens) {
      for (TextColor color : TextColor.values()) {
        if (color.name().equalsIgnoreCase(token)) {
          savedColor = color;
          continue outer;
        }
      }
      componentBuilder.append(new TextComponent(token));
      componentBuilder.color(savedColor);
    }

    return componentBuilder;
  }

  /**
   * Parses a string into the legacy chat format that is still used for some sutff in
   * minecraft...<br> Mostly handles color variables
   *
   * @param string the input string
   * @return the properly formatted legacy string
   */
  @Nonnull
  public static String parseLegacyFormat(@Nonnull String string) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] tokens = string.split("\\{|}");
    TextColor savedColor = TextColor.WHITE;
    outer:
    for (String token : tokens) {
      for (TextColor color : TextColor.values()) {
        if (color.name().equalsIgnoreCase(token)) {
          savedColor = color;
          continue outer;
        }
      }
      // why don't you just expose getCode?....
      stringBuilder.append(ChatColor.COLOR_CHAR).append(savedColor.toString().substring(1, 1)) // no COLOR_CHAR in text
          .append(token);
    }

    return stringBuilder.toString();
  }

  /**
   * Sends the user a message that contains the translated version (using his local) of the
   * specified key
   *
   * @param user the user that should receive the message
   * @param key the lang key that should be translated
   */
  public static void msg(@Nonnull User user, @Nonnull LangKey key) {
    user.sendMessage(trans(key));
  }

  /**
   * Sends the user a message that contains the translated version (using his local) of the
   * specified key<br> The specified arguments are used to fill out placeholders
   *
   * @param user the user that should receive the message
   * @param key the lang key that should be translated
   * @param args the args that should be replacing placeholders
   */
  public static void msg(@Nonnull User user, @Nonnull LangKey key, @Nullable Object... args) {
    user.sendMessage(trans(key, args));
  }

  /**
   * Translates the specified lang key into a string
   *
   * @param key the key to translate
   * @return the translated string
   */
  @Nonnull
  public static String string(@Nonnull LangKey key) {
    return string(key, handler.getDefaultLocale());
  }

  /**
   * Translates the specified lang key into a string<br> The specified arguments are used to fill
   * out placeholders
   *
   * @param key the key to translate
   * @param args the args that should be replacing placeholders
   * @return the translated string
   */
  @Nonnull
  public static String string(@Nonnull LangKey key, @Nonnull Object... args) {
    return string(key, handler.getDefaultLocale(), args);
  }

  /**
   * Translates the specified lang key into a string<br> Allows to specify a locale that should be
   * used to translate
   *
   * @param key the key to translate
   * @param loc the locale that should be used to translate the key
   * @return the translated string
   */
  @Nonnull
  public static String string(@Nonnull LangKey key, @Nonnull Locale loc) {
    return string(key, loc, new Object[0]);
  }

  /**
   * Translates the specified lang key into a string<br> Allows to specify a locale that should be
   * used to translate<br> The specified arguments are used to fill out placeholders
   *
   * @param key the key to translate
   * @param loc the locale that should be used to translate the key
   * @param args the args that should be replacing placeholders
   * @return the translated string
   */
  @Nonnull
  public static String string(@Nonnull LangKey key, @Nonnull Locale loc, @Nonnull Object... args) {
    if (args.length != key.getArgs().length) {
      throw new LangException(
          "Wrong arguments for LangKey " + key.name() + ": entered " + args.length + ", expected "
              + key.getArgs().length);
    }

    LangStorage storage = handler.getStorage(loc);
    String string = storage.get(key);

    for (int i = 0; i < args.length; i++) {
      String replacement;
      if (args[i] instanceof String) {
        replacement = (String) args[i];
      } else if (args[i] instanceof BaseComponent) {
        replacement = ChatUtil.toPlainText((BaseComponent) args[i]);
      } else if (args[i] instanceof BaseComponent[]) {
        replacement = ChatUtil.toPlainText((BaseComponent[]) args[i]);
      } else {
        replacement = String.valueOf(args[i]);
      }

      string = string.replace("{" + key.getArgs()[i] + "}", replacement);
    }

    return String.format(string, args);
  }

  @Nonnull
  public static String legacyColors(@Nonnull String message) {
    StringBuilder result = new StringBuilder();
    String[] tokens = message.split("\\{|}");
    outer:
    for (String token : tokens) {
      for (TextColor color : TextColor.values()) {
        if (color.name().equalsIgnoreCase(token)) {
          result.append(color);
          continue outer;
        }
      }

      result.append(token);
    }

    return result.toString();
  }

  /**
   * Sends a message to all players
   *
   * @param message the message to send
   */
  public static void broadcast(BaseComponent... message) {
    userHandler.getUsers().forEach(user -> user.sendMessage(message));
  }
}
