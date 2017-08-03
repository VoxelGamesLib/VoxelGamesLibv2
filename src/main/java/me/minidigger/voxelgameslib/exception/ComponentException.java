package me.minidigger.voxelgameslib.exception;

public class ComponentException extends RuntimeException {
    /**
     * This exception is thrown by components, typically when an invalid value is passed to them
     *
     * @see RuntimeException#RuntimeException(String)
     */
    public ComponentException(String message, String componentName) {
        super("[Component/" + componentName + "]: " + message);
    }
}
