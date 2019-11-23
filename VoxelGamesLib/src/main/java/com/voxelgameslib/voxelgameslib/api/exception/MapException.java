package com.voxelgameslib.voxelgameslib.api.exception;

import javax.annotation.Nonnull;

/**
 * Thrown when something is wrong related to maps.
 */
public class MapException extends VoxelGameLibException {

    /**
     * @param message the message that explains the issue
     */
    public MapException(@Nonnull String message) {
        super(message);
    }

    /**
     * Used for IO related exceptions
     *
     * @param message the message that explains the issue
     * @param ex      the root issue that was thrown
     */
    public MapException(@Nonnull String message, @Nonnull Exception ex) {
        super(message, ex);
    }
}
