package me.minidigger.voxelgameslib.chat;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.utils.ChatUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * A chat channel represents a collection of listeners who are able to receive messages send into
 * this channel
 */
public class ChatChannel {
    @Getter
    @Setter
    private String identifier;
    @Getter
    @Setter
    private List<User> listeners;

    @Nullable
    @Getter
    @Setter
    private Component prefix;

    /**
     * Creates a new, empty channel with a prefix
     *
     * @param identifier a unique identifier for this channel
     */
    public ChatChannel(String identifier, @Nullable Component prefix) {
        this.identifier = identifier;
        listeners = new ArrayList<>();
    }

    /**
     * Creates a new, empty channel
     *
     * @param identifier a unique identifier for this channel
     */
    public ChatChannel(String identifier) {
        this(identifier, null);
    }

    /**
     * Adds a new listener to the channel
     *
     * Listeners can hear messages, but messages they send will not be sent to this channel unless
     * it is their active channel.
     *
     * @param user the new listener
     */
    public void addListener(User user) {
        listeners.add(user);
    }

    /**
     * Removes a listener from the channel
     *
     * @param user the listener to remove
     */
    public void removeListener(User user) {
        listeners.remove(user);
    }

    /**
     * Sends a plaintext message to the channel's listeners
     *
     * @param byUser  sender
     * @param message message
     */
    public void sendMessage(User byUser, String message) {
        sendMessage(byUser, TextComponent.of(message));
    }

    /**
     * Sends a component message to the channel's listeners
     *
     * @param byUser sender
     */
    public void sendMessage(User byUser, Component message) {
        listeners.forEach(listener -> listener.sendMessage(ChatUtil.formatChannelMessage(this, byUser.getDisplayName(), message)));
    }
}
