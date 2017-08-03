package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Thrown when something is wrong related to lang stuff
 */
public class LangException extends VoxelGameLibException {

    /**
     * @param message the message that explains the issue
     */
    public LangException(@Nonnull String message) {
        super(message);
    }


    /**
     * Used for IO related exceptions
     *
     * @param message the message that explains the issue
     * @param ex      the root issue that was thrown
     */
    public LangException(@Nonnull String message, @Nonnull IOException ex) {
        super(message, ex);
    }
}
