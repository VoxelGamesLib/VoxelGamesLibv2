package com.voxelgameslib.voxelgameslib.exception;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.game.GameMode;

import javax.annotation.Nonnull;

/**
 * Thrown when something goes wrong while starting a {@link Game}
 */
public class GameStartException extends VoxelGameLibException {

    /**
     * @param mode the gamemode that was tried to start
     * @param e    the exception that was thrown while starting
     */
    public GameStartException(@Nonnull GameMode mode, @Nonnull Exception e) {
        super("Error while starting the game " + mode.getName(), e);
    }

    /**
     * @param mode    the gamemode that was tried to start
     * @param message a message that should be displayed
     */
    public GameStartException(@Nonnull GameMode mode, @Nonnull String message) {
        super("Error while starting the game " + mode.getName() + ": " + message);
    }
}
