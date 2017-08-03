package com.voxelgameslib.voxelgameslib.components.signs;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.handler.Handler;

import lombok.extern.java.Log;

/**
 * Handles placeholder sign and interaction signs
 */
@Log
@Singleton
public class SignHandler implements Handler {

    @Inject
    private SignButtons signButtons;
    @Inject
    private SignPlaceholders signPlaceholders;

    @Override
    public void start() {
        signButtons.registerButtons();

        signPlaceholders.init();
    }

    @Override
    public void stop() {

    }

    public SignPlaceholders getSignPlaceholders() {
        return signPlaceholders;
    }

    public SignButtons getSignButtons() {
        return signButtons;
    }
}
