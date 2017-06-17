package me.minidigger.voxelgameslib.persistence;

import com.google.inject.Injector;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.handler.Handler;

import org.bukkit.Bukkit;

/**
 * Handles saving and loading of data into numerous formats
 */
@Singleton
public class PersistenceHandler implements Handler {

    @Inject
    private Injector injector;

    private PersistenceProvider activeProvider;

    @Override
    public void start() {
        // todo: dynamically choose persistence provider, named injection perhaps
        
        activeProvider = injector.getInstance(FlatFileJsonPersistenceProvider.class);
        activeProvider.start();

        Bukkit.getLogger().info("Loaded persistence provider: " + FlatFileJsonPersistenceProvider.class.getSimpleName());
    }

    @Override
    public void stop() {
        activeProvider.stop();
    }

    /**
     * @return the currently active persistence provider
     */
    @Nonnull
    public PersistenceProvider getProvider() {
        return activeProvider;
    }
}
