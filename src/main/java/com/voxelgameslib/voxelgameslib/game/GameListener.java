package com.voxelgameslib.voxelgameslib.game;

import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameLeaveEvent;
import com.voxelgameslib.voxelgameslib.exception.UserException;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.user.UserHandler;
import lombok.extern.java.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@Log
@SuppressWarnings("JavaDoc")
public class GameListener implements Listener {

    @Inject
    private GameHandler gameHandler;
    @Inject
    private UserHandler userHandler;

    @EventHandler
    public void onLeave(@Nonnull PlayerQuitEvent event) {
        User user = userHandler.getUser(event.getPlayer().getUniqueId())
                .orElseThrow(() -> new UserException(
                        "Unknown user " + event.getPlayer().getDisplayName() + "(" + event.getPlayer()
                                .getUniqueId() + ")"));
        gameHandler.getGames(event.getPlayer().getUniqueId(), true)
                .forEach((game -> game.leave(user)));
    }

    @EventHandler
    public void onL(@Nonnull GameLeaveEvent event) {
        log.finer(event.getUser().getRawDisplayName() + " left the game " + event.getGame()
                .getGameMode().getName());
    }

    @EventHandler
    public void onJ(@Nonnull GameJoinEvent event) {
        log.finer(event.getUser().getRawDisplayName() + " joined the game " + event.getGame()
                .getGameMode().getName());
    }
}
