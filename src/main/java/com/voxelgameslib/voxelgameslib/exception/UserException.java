package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

/**
 * Thrown when something goes wrong related to users
 */
public class UserException extends VoxelGameLibException {

    /**
     * @param message the message that explains the issue
     */
    public UserException(@Nonnull String message) {
        super(message);
    }
}
