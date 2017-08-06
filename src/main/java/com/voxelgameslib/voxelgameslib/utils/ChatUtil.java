package com.voxelgameslib.voxelgameslib.utils;

import com.voxelgameslib.voxelgameslib.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.user.GamePlayer;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.ComponentSerializer;

import java.lang.reflect.Method;
import javax.annotation.Nonnull;

import org.bukkit.Bukkit;

import lombok.extern.java.Log;

/**
 * Small util for chat related stuff
 */
@Log
public class ChatUtil {

    private static String NMS_PREFIX = Bukkit.getServer().getClass().getPackage().getName()
            .replace("org.bukkit.craftbukkit", "net.minecraft.server").concat(".");
    private static final Method CRAFTPLAYER_GETHANDLE_METHOD;
    private static final Method CHATSERIALIZER_A_METHOD;
    private static final Method ENTITYPLAYER_SENDMESSAGE_METHOD;

    static {
        try {
            CRAFTPLAYER_GETHANDLE_METHOD = Class.forName(
                    Bukkit.getServer().getClass().getPackage().getName().concat(".entity.CraftPlayer"))
                    .getDeclaredMethod("getHandle");
            CHATSERIALIZER_A_METHOD = Class
                    .forName(NMS_PREFIX.concat("IChatBaseComponent$ChatSerializer"))
                    .getDeclaredMethod("a", String.class);
            ENTITYPLAYER_SENDMESSAGE_METHOD = Class.forName(NMS_PREFIX.concat("EntityPlayer"))
                    .getDeclaredMethod("sendMessage", Class.forName(NMS_PREFIX.concat("IChatBaseComponent")));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialises a array of base components into a single string (by calling #toPlainText on them)
     *
     * @param comps the base components to serialize into text
     * @return the readable text
     */
    @Nonnull
    public static String toPlainText(@Nonnull Component... comps) {
        StringBuilder sb = new StringBuilder();
        for (Component comp : comps) {
            toPlainText(sb, comp);
        }
        return sb.toString();
    }

    private static void toPlainText(@Nonnull StringBuilder sb, @Nonnull Component component) {
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
     * @param message  the message to send to the user
     */
    public static void sendMessage(@Nonnull GamePlayer gameUser, @Nonnull Component message) {
        try {
            ENTITYPLAYER_SENDMESSAGE_METHOD.invoke(CRAFTPLAYER_GETHANDLE_METHOD.invoke(gameUser.getPlayer()),
                    CHATSERIALIZER_A_METHOD.invoke(null, ComponentSerializer.serialize(message)));
        } catch (Exception e) {
            throw new RuntimeException("wut", e);
        }
    }

    @Nonnull
    public static Component formatChannelMessage(@Nonnull ChatChannel channel, @Nonnull Component displayName, @Nonnull Component message) {
        Component prefix = TextComponent.of("");

        if (channel.getPrefix() != null) {
            prefix.append(channel.getPrefix());
        }

        return TextComponent.of("")
                .append(prefix)
                .append(displayName)
                .append(TextComponent.of(": ").color(TextColor.WHITE))
                .append(message);
    }
}
