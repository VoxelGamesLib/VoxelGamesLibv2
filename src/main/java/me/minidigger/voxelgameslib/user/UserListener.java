package me.minidigger.voxelgameslib.user;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Singleton
@SuppressWarnings("JavaDoc")// no need for javadoc on event listeners
public class UserListener implements Listener {

  @Inject
  private UserHandler handler;

  @EventHandler
  public void onAsyncLogin(@Nonnull AsyncPlayerPreLoginEvent event) {
    if (!handler.login(event.getUniqueId())) {
      // something went horribly wrong
      // we don't have a locale here since the data was not loaded :/
      event.disallow(Result.KICK_OTHER, Lang.legacyColors(Lang.string(LangKey.DATA_NOT_LOADED)));
    }
  }

  @EventHandler
  public void onLogin(@Nonnull PlayerLoginEvent event) {
    if (!handler.hasLoggedIn(event.getPlayer().getUniqueId())) {
      // worst case: load data sync
      boolean login = handler.login(event.getPlayer().getUniqueId());
      if (!login || !handler.hasLoggedIn(event.getPlayer().getUniqueId())) {
        // something went horribly wrong
        // we don't have a locale here since the data was not loaded :/
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
            Lang.legacyColors(Lang.string(LangKey.DATA_NOT_LOADED)));
        return;
      }
    }

    handler.join(event.getPlayer().getUniqueId(), event.getPlayer());
  }

  @EventHandler
  public void onJoin(@Nonnull PlayerJoinEvent event) {
    // tp to spawn
    event.getPlayer().teleport(server.getSpawn().getFirst(), server.getSpawn().getSecond());
  }

  @EventHandler
  public void onLeave(@Nonnull PlayerQuitEvent event) {
    handler.logout(event.getPlayer().getUniqueId());
  }
}
