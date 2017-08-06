package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

public class ComponentException extends RuntimeException {
    /**
     * This exception is thrown by components, typically when an invalid value is passed to them
     *
     * @see RuntimeException#RuntimeException(String)
     */
    public ComponentException(@Nonnull String message, @Nonnull String componentName) {
        super("[Component/" + componentName + "]: " + message);
    }
}
