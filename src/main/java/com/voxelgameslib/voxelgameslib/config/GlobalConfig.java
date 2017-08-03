package com.voxelgameslib.voxelgameslib.config;

import com.google.gson.annotations.Expose;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.persistence.PersistenceConfig;
import com.voxelgameslib.voxelgameslib.utils.CollectionUtil;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import java.util.List;
import java.util.logging.Level;

/**
 * The config defines all configuration values (and the default values)
 */
@Singleton
public class GlobalConfig extends Config {

    public final int configVersion = 2;
    @Expose
    public int currentVersion = configVersion;

    @Expose
    public String logLevel = Level.INFO.getName();
    @Expose
    public boolean useRoleSystem = true;
    @Expose
    public List<String> availableLanguages = CollectionUtil.toStringList(Locale.values(), Locale::getTag);
    @Expose
    public Locale defaultLocale = Locale.ENGLISH;
    @Expose
    public PersistenceConfig persistence = new PersistenceConfig();
    @Expose
    public int signUpdateInterval = 60;
    @Expose
    public boolean announceNewGame = true;
    @Expose
    public boolean loadGameDefinitions = false;

    /**
     * @return the default config, with all default settings
     */
    @Nonnull
    public static GlobalConfig getDefault() {
        return new GlobalConfig();
    }

    @Override
    public int getConfigVersion() {
        return configVersion;
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }

    @Override
    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }
}
