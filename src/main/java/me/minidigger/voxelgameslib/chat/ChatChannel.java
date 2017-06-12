package me.minidigger.voxelgameslib.chat;

import lombok.Getter;
import lombok.Setter;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.utils.ChatUtil;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatChannel {
    @Getter @Setter private String identifier;
    @Getter @Setter private List<User> listeners;

    public ChatChannel(String identifier) {
        this.identifier = identifier;
        listeners = new ArrayList<>();
    }

    /**
     * Adds a new listener to the channel
     *
     * Listeners can hear messages, but messages they send will not be sent to this channel unless it is their active channel.
     *
     * @param user
     */
    public void addListener(User user) {
        listeners.add(user);
    }

    /**
     * Removes a listener from the channel
     *
     * @param user
     */
    public void removeListener(User user) {
        listeners.remove(user);
    }

    /**
     * Sends a plaintext message to the channel's listeners
     *
     * @param byUser sender
     * @param message message
     */
    public void sendMessage(User byUser, String message) {
        listeners.forEach(listener -> listener.sendMessage(ChatUtil.formatChannelMessage(byUser.getRawDisplayName(), new TextComponent(message))));
    }

    /**
     * Sends a component message to the channel's users
     *
     * @param byUser sender
     * @param message
     */
    public void sendMessage(User byUser, Component message) {
        listeners.forEach(listener -> listener.sendMessage(ChatUtil.formatChannelMessage(byUser.getRawDisplayName(), message)));
    }
}
