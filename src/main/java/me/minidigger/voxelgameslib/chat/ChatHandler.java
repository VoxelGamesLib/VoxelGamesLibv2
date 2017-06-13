package me.minidigger.voxelgameslib.chat;

import net.kyori.text.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.user.User;

@Singleton
public class ChatHandler implements Handler {
    public ChatChannel defaultChannel;
    public Map<String, ChatChannel> activeChannels;

    /**
     * @see Handler#start()
     */
    public void start() {
        // every user should be in this channel:
        defaultChannel = new ChatChannel("default");

        activeChannels = new HashMap<>();
        activeChannels.put("default", defaultChannel);
    }

    /**
     * @see Handler#stop()
     */
    public void stop() {
        defaultChannel = null;
        activeChannels = null;
    }

    /**
     * Get a channel by its identifier
     *
     * @param identifier string identifier of the channel
     * @return ChatChannel
     */
    public Optional<ChatChannel> getChannel(String identifier) {
        return Optional.ofNullable(activeChannels.get(identifier));
    }

    /**
     * Broadcast a message to all channels
     *
     * @param user    sender
     * @param message message
     */
    public void broadcastMessage(User user, String message) {
        for (ChatChannel channel : activeChannels.values()) {
            channel.sendMessage(user, message);
        }
    }

    /**
     * Broadcast a message to all channels
     *
     * @param user      sender
     * @param component message
     */
    public void broadcastMessage(User user, Component component) {
        for (ChatChannel channel : activeChannels.values()) {
            channel.sendMessage(user, component);
        }
    }
}
