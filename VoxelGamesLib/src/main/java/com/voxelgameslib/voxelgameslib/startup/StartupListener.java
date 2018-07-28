package com.voxelgameslib.voxelgameslib.startup;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@Singleton
public class StartupListener implements Listener {

    @Inject
    private StartupHandler startupHandler;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (!startupHandler.isReady()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Server is starting, please gimme a second!");
        }

        if (startupHandler.isInterrupted()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Server start was interrupted, please try again later");
        }
    }
}
