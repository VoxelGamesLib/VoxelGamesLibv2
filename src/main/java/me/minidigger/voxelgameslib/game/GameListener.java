package me.minidigger.voxelgameslib.game;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
@Log
@EventListener
@SuppressWarnings("JavaDoc")
public class GameListener {

    @Inject
    private GameHandler gameHandler;
    @Inject
    private VGLEventHandler eventHandler;

    @EventListener
    public void onLeave(@Nonnull UserLeaveEvent event) {
        gameHandler.getGames(event.getUser(), true).forEach((game -> game.leave(event.getUser())));
    }

    @EventListener
    public void onL(@Nonnull GameLeaveEvent event) {
        log.finer(event.getUser().getData().getDisplayName() + " left the game " + event.getGame().getGameMode().getName());
    }

    @EventListener
    public void onJ(@Nonnull GameJoinEvent event) {
        log.finer(event.getUser().getData().getDisplayName() + " joined the game " + event.getGame().getGameMode().getName());
    }
}
