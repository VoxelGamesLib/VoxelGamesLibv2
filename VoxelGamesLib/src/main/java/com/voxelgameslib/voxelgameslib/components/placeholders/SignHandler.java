package com.voxelgameslib.voxelgameslib.components.placeholders;

import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.handler.Handler;

/**
 * Handles placeholder sign and interaction signs
 */
@Singleton
public class SignHandler implements Handler {

    private static final Logger log = Logger.getLogger(SignHandler.class.getName());
    @Inject
    private SignButtons signButtons;
    @Inject
    private SignPlaceholders signPlaceholders;

    @Override
    public void enable() {
        signButtons.registerButtons();

        signPlaceholders.init();
    }

    @Override
    public void disable() {

    }

    @Nonnull
    public SignPlaceholders getSignPlaceholders() {
        return signPlaceholders;
    }

    @Nonnull
    public SignButtons getSignButtons() {
        return signButtons;
    }
}
