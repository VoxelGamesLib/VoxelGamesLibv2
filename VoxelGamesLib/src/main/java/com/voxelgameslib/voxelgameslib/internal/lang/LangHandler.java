package com.voxelgameslib.voxelgameslib.internal.lang;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.internal.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.internal.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;

/**
 * Handles the languages. holds all lang storages and registered languages.
 */
@Singleton
public class LangHandler implements Handler {

    private static final Logger log = Logger.getLogger(LangHandler.class.getName());
    private Locale defaultLocale;

    private Map<Locale, LangStorage> storages = new HashMap<>();
    private LangStorage defaultStorage;

    private Map<UUID, Map<Locale, ExternalLangStorage>> externalStorages = new HashMap<>();
    private Map<UUID, ExternalLangStorage> externalDefaultStorages = new HashMap<>();

    @Inject
    private VoxelGamesLib voxelGameLib;
    @Inject
    private ConfigHandler configHandler;

    @Override
    public void enable() {
        defaultLocale = configHandler.get().defaultLocale;

        defaultStorage = voxelGameLib.getInjector().getInstance(LangStorage.class);
        defaultStorage.setTranslatable(LangKey.DUMMY);
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
    public void disable() {

    }

    /**
     * Registers a new locale. also loads the file and migrates it if needed
     *
     * @param loc the locale to load
     */
    public void registerLocale(@Nonnull Locale loc) {
        LangStorage s = voxelGameLib.getInjector().getInstance(LangStorage.class);
        s.setTranslatable(LangKey.DUMMY);
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
     * Gets the storage for that lang. if there is no storage for that lang, the default storage is returned;
     *
     * @param loc the locale to get the storage from
     * @return the storage for that locale, or the default storage if the locale was not loaded
     */
    @Nonnull
    public LangStorage getStorage(@Nonnull Locale loc) {
        return storages.getOrDefault(loc, defaultStorage);
    }

    @Nonnull
    public LangStorage getExternalStorage(@Nonnull UUID id, @Nonnull Locale loc) {
        return externalStorages.get(id).getOrDefault(loc, externalDefaultStorages.get(id));
    }

    /**
     * @return the default lang storage
     */
    @Nonnull
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

    /**
     * Registers a new external provider for translation keys, will also try to register all known locales
     *
     * @param externalTranslatable the new locale key provider
     * @param langFolder           the folder the files for this provider will be stored in
     */
    public void registerExternalLangProvider(@Nonnull ExternalTranslatable externalTranslatable, @Nonnull File langFolder) {
        ExternalLangStorage defaultStorage = voxelGameLib.getInjector().getInstance(ExternalLangStorage.class);
        defaultStorage.setTranslatable(externalTranslatable);
        defaultStorage.setLangFolder(langFolder);
        defaultStorage.setLocale(defaultLocale);
        defaultStorage.load();
        externalDefaultStorages.put(externalTranslatable.getUuid(), defaultStorage);

        storages.keySet().forEach(locale -> registerExternalLocale(locale, externalTranslatable, langFolder));
    }

    /**
     * Registers a external lang storage for the given external lang key provider and the locale
     *
     * @param locale               the locale to create a new storage for
     * @param externalTranslatable the provider to create a new storage for
     * @param langFolder           the folder where the file should be stored in
     */
    public void registerExternalLocale(@Nonnull Locale locale, @Nonnull ExternalTranslatable externalTranslatable, @Nonnull File langFolder) {
        ExternalLangStorage storage = voxelGameLib.getInjector().getInstance(ExternalLangStorage.class);
        storage.setLangFolder(langFolder);
        storage.setTranslatable(externalTranslatable);
        storage.setLocale(locale);
        storage.setParentStorage(externalDefaultStorages.get(externalTranslatable.getUuid()));
        storage.load();

        int counter = storage.processNewValues();
        if (counter > 0) {
            log.info("Migrated lang file " + storage.getLangFile().getAbsolutePath() + ": Added " + counter
                    + " new keys!");
        }

        externalStorages.computeIfAbsent(externalTranslatable.getUuid(), (id) -> new HashMap<>()).put(locale, storage);
    }
}
