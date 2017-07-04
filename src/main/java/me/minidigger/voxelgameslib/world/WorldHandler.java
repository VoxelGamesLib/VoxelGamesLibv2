package me.minidigger.voxelgameslib.world;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.config.ConfigHandler;
import me.minidigger.voxelgameslib.exception.MapException;
import me.minidigger.voxelgameslib.exception.WorldException;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.map.Map;
import me.minidigger.voxelgameslib.map.MapInfo;
import me.minidigger.voxelgameslib.utils.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Handles the worlds (loading, unloading etc)
 */
@Log
@Singleton
public class WorldHandler implements Handler, Provider<WorldConfig> {

    @Inject
    @Named("WorldsFolder")
    private File worldsFolder;

    @Getter
    @Inject
    @Named("WorldContainer")
    private File worldContainer;

    @Inject
    private ConfigHandler configHandler;

    @Inject
    private Gson gson;

    @Getter
    @Inject
    private WorldRepository worldRepository;

    private WorldConfig config;
    private File configFile;

    private final List<Map> maps = new ArrayList<>();

    /**
     * Gets a map from a list of loaded maps
     *
     * @param name the map to search for
     * @return the map, if present
     */
    @Nonnull
    public Optional<Map> getMap(@Nonnull String name) {
        return maps.stream().filter(map -> map.getWorldName().equalsIgnoreCase(name)).findAny();
    }

    /**
     * Tries to load the map data for a name
     *
     * @param name the name of the map to load
     * @return the loaded map
     * @throws MapException when
     */
    @Nonnull
    public Map loadMap(@Nonnull String name) {
        Optional<Map> map = getMap(name);
        if (!map.isPresent()) {
            if (!getMapInfo(name).isPresent()) {
                throw new MapException(
                        "Unknown map " + name + ". Did you register it into the world config?");
            }

            try {
                ZipFile zipFile = new ZipFile(new File(worldsFolder, name + ".zip"));
                for (FileHeader header : (List<FileHeader>) zipFile.getFileHeaders()) {
                    if (header.getFileName().endsWith("config.json")) {
                        InputStream stream = zipFile.getInputStream(header);
                        Map m = gson.fromJson(new JsonReader(new InputStreamReader(stream)), Map.class);
                        maps.add(m);
                        return m;
                    }
                }
                throw new MapException("Could not load map config for map " + name
                        + ". Fileheader was null. Does it has a map.json?");

            } catch (Exception e) {
                throw new MapException("Error while trying to load map config " + name, e);
            }
        } else {
            return map.get();
        }
    }

    /**
     * Searches for that map info in the world config
     *
     * @param name the name of the map info to search
     * @return the found map info, if present
     */
    @Nonnull
    public Optional<MapInfo> getMapInfo(String name) {
        return config.maps.stream().filter(mapInfo -> mapInfo.getName().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Loads a world. Needs to copy the file from the repo, unzip it and let the implementation load
     * it <br><b>Always needs to call super! Super needs to be called first (because it copies the
     * world)</b>
     *
     * @param map the map that should be loaded
     * @throws WorldException something goes wrong
     */
    public void loadWorld(@Nonnull Map map) {
        map.setLoaded(true);

        try {
            ZipFile zip = new ZipFile(new File(worldsFolder, map.getWorldName() + ".zip"));
            zip.extractAll(new File(worldContainer, map.getWorldName()).getAbsolutePath());
        } catch (ZipException e) {
            throw new WorldException("Could not unzip world " + map.getWorldName() + ".", e);
        }

        loadLocalWorld(map.getWorldName());
    }

    /**
     * Unloads a world. Needs to lets the implementation unload the world and delete the folder
     * <br><b>Always needs to call super! Super needs to be called last (because it deletes the
     * world folder)</b>
     *
     * @param map the map that should be unloaded.
     */
    public void unloadWorld(@Nonnull Map map) {
        unloadLocalWorld(map.getWorldName());
        map.setLoaded(false);

        FileUtils.delete(new File(worldContainer, map.getWorldName()));
    }

    @Override
    public void start() {
        //worldRepository.setURL();// TODO make url configurable
        if (!worldsFolder.exists()) {
            log.warning(
                    "Could not find worlds folder " + worldsFolder.getAbsolutePath() + ". Clonging from "
                            + worldRepository.getURL() + "...");
            worldRepository.cloneRepo();
        }

        configFile = new File(worldsFolder, "worlds.json");

        if (!configFile.exists()) {
            log.warning("Did not found world config, saving default");
            config = WorldConfig.getDefault();
            configHandler.saveConfig(configFile, config);
        } else {
            log.info("Loading world config");
            config = configHandler.loadConfig(configFile, WorldConfig.class);

            if (configHandler.checkMigrate(config)) {
                configHandler.migrate(configFile, config);
            }
        }
    }

    @Override
    public void stop() {

    }

    @Nonnull
    @Override
    public WorldConfig get() {
        return config;
    }

    /**
     * Loads a local world
     *
     * @param name the world to load
     * @throws WorldException if the world is not found or something else goes wrong
     */
    public void loadLocalWorld(@Nonnull String name) {
        org.bukkit.WorldCreator wc = new WorldCreator(name);
        wc.environment(World.Environment.NORMAL); //TODO do we need support for environment in maps?
        wc.generateStructures(false);
        wc.type(WorldType.NORMAL);
        wc.generator(new CleanRoomChunkGenerator());
        wc.generatorSettings("");
        World world = wc.createWorld();
        world.setAutoSave(false);
    }

    /**
     * Unloads a local world
     *
     * @param name the world to load
     * @throws WorldException if the world is not found or something else goes wrong
     */
    public void unloadLocalWorld(@Nonnull String name) {
        Bukkit.unloadWorld(name, false);
    }

    /**
     * @return the folder where the playerable worlds are saved in (think about it as a repo for
     * worlds/maps)
     */
    @Nonnull
    public File getWorldsFolder() {
        return worldsFolder;
    }

    /**
     * saves the worldconfig
     */
    public void saveConfig() {
        configHandler.saveConfig(configFile, config);
    }
}
