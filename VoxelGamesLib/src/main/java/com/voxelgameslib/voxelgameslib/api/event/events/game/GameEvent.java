package com.voxelgameslib.voxelgameslib.api.event.events.game;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.VoxelGamesLibEvent;
import com.voxelgameslib.voxelgameslib.api.game.Game;

/**
 * Parent class for game events
 */
public abstract class GameEvent extends VoxelGamesLibEvent {

    private Game game;

    GameEvent(@Nonnull Game game) {
        this.game = game;
    }

    /**
     * @return the game involved with this event
     */
    @Nonnull
    public Game getGame() {
        return game;
    }
}
