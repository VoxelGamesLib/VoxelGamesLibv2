package me.minidigger.voxelgameslib.persistence;

import com.google.inject.Injector;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.handler.Handler;

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
//        activeProvider = injector.getInstance(HibernatePersistenceProvider.class);//TODO bind this?
//        activeProvider.start(); //TODO fix persistence
    activeProvider = injector.getInstance(FlatFileJsonPersistenceProvider.class);
    activeProvider.start();
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
