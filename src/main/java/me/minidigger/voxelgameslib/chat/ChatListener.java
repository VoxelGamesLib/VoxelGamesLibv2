package me.minidigger.voxelgameslib.chat;

import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.inject.Inject;

public class ChatListener implements Listener {
    @Inject
    private ChatHandler chatHandler;
    @Inject
    private UserHandler userHandler;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        User user = userHandler.getUser(event.getPlayer().getUniqueId()).isPresent() ? userHandler.getUser(event.getPlayer().getUniqueId()).get() : null;

        if (user != null) {
            user.getActiveChannel().sendMessage(user, event.getMessage());
        }

        event.setCancelled(true);
    }
}
