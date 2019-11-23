package com.voxelgameslib.voxelgameslib.api.exception;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.Game;
import com.voxelgameslib.voxelgameslib.api.game.GameMode;

/**
 * Thrown when something goes wrong while starting a {@link Game}
 */
public class GameStartException extends VoxelGameLibException {

    /**
     * @param mode the gamemode that was tried to enable
     * @param e    the exception that was thrown while starting
     */
    public GameStartException(@Nonnull GameMode mode, @Nonnull Exception e) {
        super("Error while starting the game " + mode.getName(), e);
    }

    /**
     * @param mode    the gamemode that was tried to enable
     * @param message a message that should be displayed
     */
    public GameStartException(@Nonnull GameMode mode, @Nonnull String message) {
        super("Error while starting the game " + mode.getName() + ": " + message);
    }
}
