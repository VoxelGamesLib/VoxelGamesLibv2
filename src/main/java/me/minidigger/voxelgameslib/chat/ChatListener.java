package me.minidigger.voxelgameslib.chat;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Singleton
public class ChatListener implements Listener {
    @Inject
    private ChatHandler chatHandler;
    @Inject
    private UserHandler userHandler;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Optional<User> user = userHandler.getUser(event.getPlayer().getUniqueId());

        user.ifPresent(user1 -> user1.getActiveChannel().sendMessage(user1, event.getMessage()));

        event.setCancelled(true);
    }
}
