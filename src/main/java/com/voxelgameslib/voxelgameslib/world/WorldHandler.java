package com.voxelgameslib.voxelgameslib.world;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.exception.MapException;
import com.voxelgameslib.voxelgameslib.exception.WorldException;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.map.Map;
import com.voxelgameslib.voxelgameslib.map.MapInfo;
import com.voxelgameslib.voxelgameslib.map.MapScanner;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.FileUtils;
import com.voxelgameslib.voxelgameslib.utils.NMSUtil;
import com.voxelgameslib.voxelgameslib.utils.ZipUtil;
import lombok.Getter;
import lombok.extern.java.Log;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.bukkit.*;
import org.bukkit.WorldCreator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

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
    @Inject
    private MapScanner mapScanner;
    @Getter
    @Inject
    private WorldRepository worldRepository;
    @Inject
    private VoxelGamesLib voxelGamesLib;

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
     * @param map    the map that should be loaded
     * @param gameid the id of the game this map belongs to
     * @throws WorldException something goes wrong
     */
    public void loadWorld(@Nonnull Map map, UUID gameid, boolean replaceMarkers) {
        map.load(gameid, "TEMP_" + map.getWorldName() + "_" + gameid.toString().split("-")[0]);
        log.finer("Loading map " + map.getInfo().getName() + " as " + map.getLoadedName(gameid));

        File file = new File(worldContainer, map.getLoadedName(gameid));

        try {
            ZipFile zip = new ZipFile(new File(worldsFolder, map.getWorldName() + ".zip"));
            zip.extractAll(file.getAbsolutePath());
            FileUtils.delete(new File(file, "uid.dat"));
        } catch (ZipException e) {
            throw new WorldException("Could not unzip world " + map.getInfo().getName() + ".", e);
        }

        World world = loadLocalWorld(map.getLoadedName(gameid));

        if (replaceMarkers) {
            replaceMarkers(world, map);
        }
    }

    /**
     * Unloads a world. Needs to lets the implementation unload the world and delete the folder
     * <br><b>Always needs to call super! Super needs to be called last (because it deletes the
     * world folder)</b>
     *
     * @param map    the map that should be unloaded.
     * @param gameid the id of the game that this map belongs to
     */
    public void unloadWorld(@Nonnull Map map, UUID gameid) {
        unloadLocalWorld(map.getLoadedName(gameid));
        FileUtils.delete(new File(worldContainer, map.getLoadedName(gameid)));
        map.unload(gameid);
    }

    /**
     * Replaces the marker blocks with
     *
     * @param world the world in which the markers are located
     * @param map   the map defining the markers
     */
    public void replaceMarkers(World world, Map map) {
        map.getMarkers().forEach(marker -> marker.getLoc().toLocation(world.getName()).getBlock().setType(Material.AIR));
        log.finer("Replaced " + map.getMarkers().size() + " markers with air");
        //TODO chest markers?
    }

    @Override
    public void start() {
        cleanup();

        //worldRepository.setURL();// TODO make url configurable
        if (!worldsFolder.exists()) {
            log.warning(
                    "Could not find worlds folder " + worldsFolder.getAbsolutePath() + ". Clonging from "
                            + worldRepository.getURL() + "...");
            worldRepository.cloneRepo();
        } else {
            worldRepository.updateRepo();
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
        cleanup();
    }

    private void cleanup() {
        File[] files = worldContainer.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().startsWith("TEMP_")) {
                    FileUtils.delete(file);
                }
            }
        }
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
     * @return the loaded world
     * @throws WorldException if the world is not found or something else goes wrong
     */
    public World loadLocalWorld(@Nonnull String name) {
        org.bukkit.WorldCreator wc = new WorldCreator(name);
        wc.environment(World.Environment.NORMAL); //TODO do we need support for environment in maps?
        wc.generateStructures(false);
        wc.type(WorldType.NORMAL);
        wc.generator(new CleanRoomChunkGenerator());
        wc.generatorSettings("");
        World world = wc.createWorld();
        world.setAutoSave(false);
        return world;
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

    public void finishWorldEditing(User editor, Map map) {
        World world = Bukkit.getWorld(map.getLoadedName(editor.getUuid()));
        world.setSpawnLocation((int) map.getCenter().getX(), (int) map.getCenter().getY(), (int) map.getCenter().getZ());
        world.setAutoSave(true);
        world.save();

        NMSUtil.flushSaveQueue(world);

        mapScanner.scan(map, editor.getUuid());

        File worldFolder = new File(getWorldContainer(), map.getWorldName());

        try {
            FileWriter fileWriter = new FileWriter(new File(worldFolder, "config.json"));
            gson.toJson(map, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            Lang.msg(editor, LangKey.WORLD_CREATOR_SAVE_CONFIG_ERROR, e.getMessage(),
                    e.getClass().getName());
            log.log(Level.WARNING, "Error while saving the world config", e);
            return;
        }

        ZipFile zip;
        try {
            zip = ZipUtil.createZip(worldFolder);
        } catch (ZipException e) {
            Lang.msg(editor, LangKey.WORLD_CREATOR_SAVE_ZIP_ERROR, e.getMessage(),
                    e.getClass().getName());
            log.log(Level.WARNING, "Error while creating the zip", e);
            return;
        }

        try {
            File to = new File(getWorldsFolder(), zip.getFile().getName());
            if (to.exists()) {
                if (!to.delete()) {
                    log.warning("Could not delete " + to.getName());
                }
            }
            Files.move(zip.getFile(), to);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!config.maps.contains(map.getInfo())) {
            config.maps.add(map.getInfo());
            saveConfig();
        }

        getWorldRepository().commitRepo();

        Lang.msg(editor, LangKey.WORLD_CREATOR_DONE);
    }
}
