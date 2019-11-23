package com.voxelgameslib.voxelgameslib.internal.persistence;

import com.google.inject.Injector;

import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.internal.handler.Handler;

/**
 * Handles saving and loading of data into numerous formats
 */
@Singleton
public class PersistenceHandler implements Handler {

    private static final Logger log = Logger.getLogger(PersistenceHandler.class.getName());
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
