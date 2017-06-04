package me.minidigger.voxelgameslib.user;

import com.google.inject.Injector;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Singleton
@SuppressWarnings("JavaDoc")// no need for javadoc on event listeners
public class UserListener implements Listener{

  @Inject
  private UserHandler handler;
  @Inject
  private Injector injector;

  @EventHandler
  public void onAsyncLogin(@Nonnull AsyncUserLoginEvent event) {
    if (!handler.login(event.getUuid())) {
      // something went horribly wrong
      event.setCanceled(true);
      // we don't have a locale here since the data was not loaded :/
      event.setKickMessage(Lang.legacyColors(Lang.string(LangKey.DATA_NOT_LOADED)));
    }
  }

  @EventHandler
  public void onLogin(@Nonnull UserLoginEvent event) {
    if (!handler.hasLoggedIn(event.getUuid())) {
      // worst case: load data sync
      boolean login = handler.login(event.getUuid());
      if (!login || !handler.hasLoggedIn(event.getUuid())) {
        // something went horribly wrong
        event.setCanceled(true);
        // we don't have a locale here since the data was not loaded :/
        event.setKickMessage(Lang.legacyColors(Lang.string(LangKey.DATA_NOT_LOADED)));
        return;
      }
    }

    handler.join(event.getUuid(), event.getPlayerObject());
  }

  @EventHandler
  public void onJoin(@Nonnull UserJoinEvent event) {
    // tp to spawn
    event.getUser().teleport(server.getSpawn().getFirst(), server.getSpawn().getSecond());
  }

  @EventHandler
  public void onLeave(@Nonnull UserLeaveEvent event) {
    handler.logout(event.getUser().getUuid());
  }
}
