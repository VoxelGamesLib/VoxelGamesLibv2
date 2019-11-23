package com.voxelgameslib.voxelgameslib.components.chat;

import net.kyori.text.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.components.user.User;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;

/**
 * Handles everything related to chat
 */
@Singleton
public class ChatHandler implements Handler {

    public ChatChannel defaultChannel;
    public Map<String, ChatChannel> activeChannels;

    /**
     * @see Handler#enable()
     */
    @Override
    public void enable() {
        // every user should be in this channel:
        defaultChannel = new ChatChannel("default");

        activeChannels = new HashMap<>();
        activeChannels.put("default", defaultChannel);
    }

    /**
     * @see Handler#disable()
     */
    @Override
    public void disable() {
        defaultChannel = null;
        activeChannels = null;
    }

    /**
     * Get a channel by its identifier
     *
     * @param identifier string identifier of the channel
     * @return the ChatChannel, if present
     */
    @Nonnull
    public Optional<ChatChannel> getChannel(@Nonnull String identifier) {
        return Optional.ofNullable(activeChannels.get(identifier));
    }

    /**
     * Broadcast a message to all channels
     *
     * @param user    sender the sender of the message
     * @param message message the message to broadcast
     */
    public void broadcastMessage(@Nonnull User user, @Nonnull String message) {
        for (ChatChannel channel : activeChannels.values()) {
            channel.sendMessage(user, message);
        }
    }

    /**
     * Broadcast a message to all channels
     *
     * @param user      sender the sender of the message
     * @param component message the message to broadcast
     */
    public void broadcastMessage(@Nonnull User user, @Nonnull Component component) {
        for (ChatChannel channel : activeChannels.values()) {
            channel.sendMessage(user, component);
        }
    }

    /**
     * Creates a new chat channel with the given id
     *
     * @param id the id the channel should have
     * @return the created channel
     */
    @Nonnull
    public ChatChannel createChannel(@Nonnull String id) {
        ChatChannel chatChannel = new ChatChannel(id);
        activeChannels.put(id, chatChannel);
        return chatChannel;
    }

    /**
     * Removes a channel
     *
     * @param id the id of the channel that should be removed
     */
    public void removeChannel(@Nonnull String id) {
        // remove any existing listeners from the channel, set them to the default channel.
        // you should *really* handle this yourself if you're removing a channel

        getChannel(id).ifPresent(chatChannel -> {
            Iterator<User> itr = chatChannel.getListeners().iterator();
            while (itr.hasNext()) {
                User user = itr.next();
                user.removeListeningChannel(id);

                if (user.getActiveChannel().equals(chatChannel)) {
                    user.setActiveChannel("default");
                }
            }
        });

        activeChannels.remove(id);
    }
}
