package com.voxelgameslib.voxelgameslib.components.chat;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.voxelgameslib.voxelgameslib.components.user.User;
import com.voxelgameslib.voxelgameslib.util.utils.ChatUtil;

/**
 * A chat channel represents a collection of listeners who are able to receive messages send into this channel
 */
public class ChatChannel {

    private String identifier;
    private List<User> listeners;
    @Nullable
    private Component prefix;

    /**
     * Creates a new, empty channel with a prefix
     *
     * @param identifier a unique identifier for this channel
     */
    ChatChannel(@Nonnull String identifier, @Nullable Component prefix) {
        this.identifier = identifier;
        this.listeners = new ArrayList<>();
        this.prefix = prefix;
    }

    /**
     * Creates a new, empty channel
     *
     * @param identifier a unique identifier for this channel
     */
    ChatChannel(@Nonnull String identifier) {
        this(identifier, null);
    }

    /**
     * Adds a new listener to the channel <p> Listeners can hear messages, but messages they send will not be sent to
     * this channel unless it is their active channel.
     *
     * @param user the new listener
     */
    public void addListener(@Nonnull User user) {
        listeners.add(user);
    }

    /**
     * Removes a listener from the channel
     *
     * @param user the listener to remove
     */
    public void removeListener(@Nonnull User user) {
        listeners.remove(user);
    }

    /**
     * Sends a plaintext message to the channel's listeners
     *
     * @param byUser  sender
     * @param message message
     */
    public void sendMessage(@Nonnull User byUser, @Nonnull String message) {
        sendMessage(byUser, TextComponent.of(message));
    }

    /**
     * Sends a component message to the channel's listeners
     *
     * @param byUser  sender
     * @param message the message to send
     */
    public void sendMessage(@Nonnull User byUser, @Nonnull Component message) {
        listeners.forEach(listener -> listener.sendMessage(ChatUtil.formatChannelMessage(this, byUser.getDisplayName(), message)));
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public List<User> getListeners() {
        return this.listeners;
    }

    @Nullable
    public Component getPrefix() {
        return this.prefix;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setListeners(List<User> listeners) {
        this.listeners = listeners;
    }

    public void setPrefix(@Nullable Component prefix) {
        this.prefix = prefix;
    }
}
