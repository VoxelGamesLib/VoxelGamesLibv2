package me.minidigger.voxelgameslib.event.events.player;

import lombok.Getter;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.user.User;

import javax.annotation.Nonnull;

/**
 * This event is called when a player is eliminated from a game.<br>Gametypes should call this themselves.
 */
public class PlayerEliminationEvent extends PlayerEvent {

    @Getter private Game game;

    public PlayerEliminationEvent(@Nonnull User user, @Nonnull Game game) {
        super(user);
        this.game = game;
    }
}
