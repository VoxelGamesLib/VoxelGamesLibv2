package com.voxelgameslib.voxelgameslib.world;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.config.Config;
import com.voxelgameslib.voxelgameslib.map.MapInfo;

/**
 * The config that knows about all world/maps that are playable
 */
public class WorldConfig extends Config {

    @Expose
    public final int configVersion = 1;
    @Expose
    public int currentVersion = configVersion;
    @Expose
    public final List<MapInfo> maps = new ArrayList<>();

    /**
     * @return the default values for this config
     */
    @Nonnull
    public static WorldConfig getDefault() {
        return new WorldConfig();
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
