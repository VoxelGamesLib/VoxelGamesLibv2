package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

/**
 * Thrown when something is wrong related to worlds
 */
public class WorldException extends VoxelGameLibException {

    /**
     * @param message the message that explains the issue
     */
    public WorldException(@Nonnull String message) {
        super(message);
    }

    /**
     * Used for IO related exceptions
     *
     * @param message the message that explains the issue
     * @param e       the root issue that was thrown
     */
    public WorldException(@Nonnull String message, @Nonnull Exception e) {
        super(message, e);
    }
}
