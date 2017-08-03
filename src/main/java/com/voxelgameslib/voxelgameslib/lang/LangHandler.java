package com.voxelgameslib.voxelgameslib.lang;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.persistence.PersistenceHandler;
import lombok.extern.java.Log;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Handles the languages. holds all lang storages and registered languages.
 */
@Log
@Singleton
public class LangHandler implements Handler {

    private final Map<Locale, LangStorage> storages = new HashMap<>();
    private Locale defaultLocale;
    private LangStorage defaultStorage;

    @Inject
    private VoxelGamesLib voxelGameLib;
    @Inject
    private PersistenceHandler persistenceHandler;
    @Inject
    private ConfigHandler configHandler;

    @Override
    public void start() {
        defaultLocale = configHandler.get().defaultLocale;

        defaultStorage = voxelGameLib.getInjector().getInstance(LangStorage.class);
        defaultStorage.setLocale(defaultLocale);
        defaultStorage.load();

        int counter = defaultStorage.processNewValues();
        if (counter > 0) {
            log.info("Migrated lang file " + defaultStorage.getLangFile().getAbsolutePath() + ": Added "
                    + counter + " new keys!");
        }

        for (String tag : voxelGameLib.getInjector()
                .getInstance(GlobalConfig.class).availableLanguages) {
            Optional<Locale> opt = Locale.fromTag(tag);
            if (opt.isPresent()) {
                registerLocale(opt.get());
            } else {
                log.warning("Unknown lang tag " + tag);
            }
        }
    }

    @Override
    public void stop() {

    }

    /**
     * Registers a new locale. also loads the file and migrates it if needed
     *
     * @param loc the locale to load
     */
    public void registerLocale(@Nonnull Locale loc) {
        LangStorage s = voxelGameLib.getInjector().getInstance(LangStorage.class);
        s.setLocale(loc);
        s.setParentStorage(defaultStorage);
        s.load();

        int counter = s.processNewValues();
        if (counter > 0) {
            log.info("Migrated lang file " + s.getLangFile().getAbsolutePath() + ": Added " + counter
                    + " new keys!");
        }

        storages.put(loc, s);
    }

    /**
     * @return the default locale used on this server
     */
    @Nonnull
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Gets the storage for that lang. if there is no storage for that lang, the default storage is
     * returned;
     *
     * @param loc the locale to get the storage from
     * @return the storage for that locale, or the default storage if the locale was not loaded
     */
    @Nonnull
    public LangStorage getStorage(@Nonnull Locale loc) {
        return storages.getOrDefault(loc, defaultStorage);
    }

    /**
     * @return the default lang storage
     */
    public LangStorage getDefaultStorage() {
        return defaultStorage;
    }

    /**
     * @return a set with all installed locales
     */
    @Nonnull
    public Set<Locale> getInstalledLocales() {
        return storages.keySet();
    }
}
