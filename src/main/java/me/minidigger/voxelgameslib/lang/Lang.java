package me.minidigger.voxelgameslib.lang;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.exception.LangException;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.user.GameConsoleUser;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import me.minidigger.voxelgameslib.utils.ChatUtil;

import org.bukkit.ChatColor;

import lombok.extern.java.Log;

/**
 * Gives quick access to the lang storage and translation and stuff
 */
@Log
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
     * Creates an ComponentBuilder based on that LangKey<br> The specified arguments are used to
     * fill out placeholders
     *
     * @param key  the lang key that should be translated
     * @param args the arguments that should be replaying placeholders
     * @return the created component builder
     */
    @Nonnull
    public static TextComponent trans(@Nonnull LangKey key, @Nullable Object... args) {
        return trans(key, handler.getDefaultLocale(), args);
    }

    /**
     * Creates an ComponentBuilder based on that LangKey<br> Allows to specify a locale that should
     * be used to translate
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
     * Creates an ComponentBuilder based on that LangKey<br> Allows to specify a locale that should
     * be used to translate<br> The specified arguments are used to fill out placeholders
     *
     * @param key  the lang key that should be translated
     * @param loc  the locale that should be used to translate the key
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
     * Parses a string into a component builder.<br> Takes care of {color} variables and handles
     * click and hover events
     *
     * @param string the input string
     * @return the outputted and properly filled component builder
     */
    @Nonnull
    public static TextComponent parseFormat(@Nonnull String string) {
        TextComponent componentBuilder = TextComponent.of("");
        String[] tokens = string.split("\\{|}");
        TextColor savedColor = TextColor.WHITE;
        ClickEvent clickEvent = null;
        boolean addClick = false;
        HoverEvent hoverEvent = null;
        boolean addHover = false;
        outer:
        for (String token : tokens) {
            // actions
            if (token.startsWith("click")) {
                addClick = false;
                if (clickEvent != null)
                    throw new VoxelGameLibException("Can't parse click action (nested) " + string);
                String[] args = token.split(":");
                if (args.length < 2)
                    throw new VoxelGameLibException("Can't parse click action (too few args) " + string);
                switch (args[1]) {
                    case "run_command":
                        clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, token.replace("click:run_command:", ""));
                        break;
                    case "suggest_command":
                        clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, token.replace("click:suggest_command:", ""));
                        break;
                    case "open_url":
                        clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, token.replace("click:open_url:", ""));
                        break;
                    case "change_page":
                        clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, token.replace("click:change_page:", ""));
                        break;
                    default:
                        throw new VoxelGameLibException("Can't parse click action (invalid type " + args[1] + ") " + string);
                }
                continue;
            } else if (token.equals("/click")) {
                addClick = true;
                token = "";
            } else if (token.startsWith("hover")) {
                addHover = false;
                if (hoverEvent != null)
                    //TODO nested hover events?
                    throw new VoxelGameLibException("Can't parse hover action (nested) " + string);
                String[] args = token.split(":");
                if (args.length < 2)
                    throw new VoxelGameLibException("Can't parse hover action (too few args) " + string);
                switch (args[1]) {
                    case "show_text":
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of(token.replace("hover:show_text:", "")));
                        break;
                    case "show_item":
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of(token.replace("hover:show_item:", "")));
                        break;
                    case "show_entity":
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of(token.replace("hover:show_entity:", "")));
                        break;
                    default:
                        throw new VoxelGameLibException("Can't parse hover action (invalid type " + args[1] + ") " + string);
                }
                continue;
            } else if (token.equals("/hover")) {
                addHover = true;
                token = "";
            }
            // color stuff
            else {
                for (TextColor color : TextColor.values()) {
                    if (color.name().equalsIgnoreCase(token)) {
                        savedColor = color;
                        continue outer;
                    }
                }
            }

            // put it together
            componentBuilder = componentBuilder.append(TextComponent.of(token).color(savedColor));
            if (addClick) {
                componentBuilder = componentBuilder.clickEvent(clickEvent);
                clickEvent = null;
            }
            if (addHover) {
                componentBuilder = componentBuilder.hoverEvent(hoverEvent);
                hoverEvent = null;
            }
        }

        if (clickEvent != null) {
            throw new VoxelGameLibException("Unfinished click tag " + string);
        }
        if (hoverEvent != null) {
            throw new VoxelGameLibException("Unfinished hover tag " + string);
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
            stringBuilder.append(ChatColor.COLOR_CHAR)
                    .append(savedColor.toString().substring(1, 1)) // no COLOR_CHAR in text
                    .append(token);
        }

        return stringBuilder.toString();
    }

    /**
     * Sends the user a message that contains the translated version (using his local) of the
     * specified key
     *
     * @param user the user that should receive the message
     * @param key  the lang key that should be translated
     */
    public static void msg(@Nonnull User user, @Nonnull LangKey key) {
        user.sendMessage(trans(key));
    }

    /**
     * Sends the user a message that contains the translated version (using his local) of the
     * specified key<br> The specified arguments are used to fill out placeholders
     *
     * @param user the user that should receive the message
     * @param key  the lang key that should be translated
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
     * @param key  the key to translate
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
     * @param key  the key to translate
     * @param loc  the locale that should be used to translate the key
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
            } else if (args[i] instanceof Component) {
                replacement = ChatUtil.toPlainText((Component) args[i]);
            } else if (args[i] instanceof Component[]) {
                replacement = ChatUtil.toPlainText((Component[]) args[i]);
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
    public static void broadcast(Component message) {
        userHandler.getUsers().forEach(user -> user.sendMessage(message));
        GameConsoleUser.INSTANCE.sendMessage(message);
    }

    /**
     * Sends a message to all players
     *
     * @param key  the message to send
     * @param args the args for the message
     */
    public static void broadcast(LangKey key, Object... args) {
        userHandler.getUsers().forEach(user -> Lang.msg(user, key, args));
        GameConsoleUser.INSTANCE.sendMessage(Lang.trans(key, args));
    }
}
