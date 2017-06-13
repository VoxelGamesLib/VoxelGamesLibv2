package me.minidigger.voxelgameslib.config;

/**
 * Superclass for config classes
 */
public abstract class Config {

    /**
     * @return the config version that this version of the plugin is providing
     */
    public abstract int getConfigVersion();

    /**
     * @return the config version that is currently saved to disk
     */
    public abstract int getCurrentVersion();

    /**
     * Change (update) the current used config version
     *
     * @param currentVersion the new config version
     */
    public abstract void setCurrentVersion(int currentVersion);
}
