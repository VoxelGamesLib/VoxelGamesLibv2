package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.exception.GameStartException;
import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.game.DefaultGameData;
import com.voxelgameslib.voxelgameslib.map.Map;
import com.voxelgameslib.voxelgameslib.world.WorldHandler;

import org.bukkit.Bukkit;
import org.bukkit.World;

import lombok.extern.java.Log;

@Log
@FeatureInfo(name = "MapFeature", author = "MiniDigger", version = "1.0",
        description = "Handles loading and unloading of the map for this phase")
public class MapFeature extends AbstractFeature {

    @Inject
    private WorldHandler worldHandler;

    private Map map;
    private World world;
    @Expose
    private boolean shouldUnload;
    @Expose
    private Type type = Type.VOTEWINNER;

    public enum Type {
        LOBBY, VOTEWINNER
    }

    @Override
    public void start() {
        // we already set the map externally, no need to do anything of the following, just set the world
        if (map != null) {
            world = Bukkit.getWorld(map.getLoadedName(getPhase().getGame().getUuid()));
            return;
        }

        DefaultGameData gameData = getPhase().getGame().getGameData(DefaultGameData.class).orElse(new DefaultGameData());
        if ((type == Type.LOBBY && gameData.lobbyMap == null) || (type == Type.VOTEWINNER && gameData.voteWinner == null)) {
            throw new GameStartException(getPhase().getGame().getGameMode(), "No map data was stored!");
        }

        String mapName;
        if (type == Type.LOBBY) {
            mapName = gameData.lobbyMap.getName();
        } else if (type == Type.VOTEWINNER) {
            mapName = gameData.voteWinner.getName();
        } else {
            throw new VoxelGameLibException("Unknown maptype");
        }

        try {
            map = worldHandler.loadMap(mapName);

            if (!map.isLoaded(getPhase().getGame().getUuid())) {
                world = worldHandler.loadWorld(map, getPhase().getGame().getUuid(), true);
            } else {
                world = Bukkit.getWorld(map.getLoadedName(getPhase().getGame().getUuid()));
            }
        } catch (Exception ex) {
            throw new GameStartException(getPhase().getGame().getGameMode(), ex);
        }
    }

    @Override
    public void stop() {
        if (shouldUnload) {
            worldHandler.unloadWorld(map, getPhase().getGame().getUuid());
        }
    }

    /**
     * @return the type of this map feature
     */
    @Nonnull
    public Type getType() {
        return type;
    }

    /**
     * sets the type of this map feature
     *
     * @param type the new type
     */
    public void setType(@Nonnull Type type) {
        this.type = type;
    }

    /**
     * @param shouldUnload if the world should be unloaded after this phase ends
     */
    public void setShouldUnload(boolean shouldUnload) {
        this.shouldUnload = shouldUnload;
    }

    /**
     * @return if the world should be unloaded after this phase ends
     */
    public boolean shouldUnload() {
        return shouldUnload;
    }

    /**
     * @return the map this phase will be played on
     */
    @Nonnull
    public Map getMap() {
        return map;
    }

    @Nonnull
    public World getWorld() {
        return world;
    }

    public void setMap(@Nonnull Map map) {
        this.map = map;
    }
}
