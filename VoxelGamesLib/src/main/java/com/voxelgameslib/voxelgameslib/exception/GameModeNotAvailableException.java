package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.game.GameMode;

/**
 * Thrown when trying to do something with a {@link GameMode} that is not registered on this server
 */
public class GameModeNotAvailableException extends VoxelGameLibException {

    /**
     * @param mode the gamemode that was tried to access
     */
    public GameModeNotAvailableException(@Nonnull GameMode mode) {
        super(
                "Gamemode " + mode.getName() + " is not available. Was it correctly installed and loaded?");
    }

}
