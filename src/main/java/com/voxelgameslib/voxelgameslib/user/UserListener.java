package com.voxelgameslib.voxelgameslib.user;

import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Log
@Singleton
@SuppressWarnings("JavaDoc")// no need for javadoc on event listeners
public class UserListener implements Listener {

    @Inject
    private UserHandler handler;
    @Inject
    private GameHandler gameHandler;

    @EventHandler
    public void onAsyncLogin(@Nonnull AsyncPlayerPreLoginEvent event) {
        if (!handler.login(event.getUniqueId())) {
            // something went horribly wrong
            // we don't have a locale here since the data was not loaded :/
            event.disallow(Result.KICK_OTHER, Lang.legacyColors(Lang.string(LangKey.DATA_NOT_LOADED)));
        }
    }

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        if (!handler.hasLoggedIn(event.getPlayer().getUniqueId())) {
            // worst case: load data sync
            log.warning("Loading data for player " + event.getPlayer().getName() + "(" + event.getPlayer()
                    .getUniqueId() + ") sync!");
            boolean login = handler.login(event.getPlayer().getUniqueId());
            if (!login || !handler.hasLoggedIn(event.getPlayer().getUniqueId())) {
                // something went horribly wrong
                // we don't have a locale here since the data was not loaded :/
                event.getPlayer().kickPlayer(Lang.legacyColors(Lang.string(LangKey.DATA_NOT_LOADED)));
                return;
            }
        }

        handler.join(event.getPlayer());

        // tp to spawn
        event.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    }

    @EventHandler
    public void onLeave(@Nonnull PlayerQuitEvent event) {
        Optional<User> user = handler.getUser(event.getPlayer().getUniqueId());
        user.ifPresent(u -> gameHandler.getGames(u.getUuid(), true).forEach(game -> game.leave(u)));

        handler.logout(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void suppressJoinMessages(@Nonnull PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void suppressQuitMessages(@Nonnull PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
