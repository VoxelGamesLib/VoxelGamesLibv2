package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

/**
 * A global exception, should not be thrown.
 */
public class VoxelGameLibException extends RuntimeException {

    /**
     * @param message the message that explains the issue
     */
    public VoxelGameLibException(@Nonnull String message) {
        super(message);
    }

    /**
     * @param message the message that explains the issue
     * @param e       the root exception that caused this exception
     */
    public VoxelGameLibException(@Nonnull String message, @Nonnull Exception e) {
        super(message, e);
    }
}
