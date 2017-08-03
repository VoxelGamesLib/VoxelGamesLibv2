package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

/**
 * Thrown when something goes wrong related to configs, like loading or saving.
 */
public class ConfigException extends VoxelGameLibException {

    /**
     * @param msg a short message with additional information
     * @param ex  the exception that was originally thrown
     */
    public ConfigException(@Nonnull String msg, @Nonnull Exception ex) {
        super(msg, ex);
    }
}
