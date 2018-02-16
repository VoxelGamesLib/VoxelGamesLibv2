package com.voxelgameslib.voxelgameslib.persistence;

import com.google.inject.Injector;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.handler.Handler;

import lombok.extern.java.Log;

/**
 * Handles saving and loading of data into numerous formats
 */
@Log
@Singleton
public class PersistenceHandler implements Handler {

    @Inject
    private Injector injector;

    private PersistenceProvider activeProvider;

    @Override
    public void enable() {
        // todo: dynamically choose persistence provider, named injection perhaps

        activeProvider = injector.getInstance(HibernatePersistenceProvider.class);
        //activeProvider = injector.getInstance(FlatFileJsonPersistenceProvider.class);
        activeProvider.enable();

        log.info("Loaded persistence provider: " + activeProvider.getClass().getSimpleName());
    }

    @Override
    public void disable() {
        activeProvider.disable();
    }

    /**
     * @return the currently active persistence provider
     */
    @Nonnull
    public PersistenceProvider getProvider() {
        return activeProvider;
    }
}
