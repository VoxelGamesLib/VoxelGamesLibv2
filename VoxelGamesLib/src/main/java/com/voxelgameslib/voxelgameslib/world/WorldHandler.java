package com.voxelgameslib.voxelgameslib.world;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.exception.MapException;
import com.voxelgameslib.voxelgameslib.exception.WorldException;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.map.Map;
import com.voxelgameslib.voxelgameslib.map.MapHandler;
import com.voxelgameslib.voxelgameslib.map.MapInfo;
import com.voxelgameslib.voxelgameslib.map.MapScanner;
import com.voxelgameslib.voxelgameslib.map.Marker;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.FileUtils;
import com.voxelgameslib.voxelgameslib.utils.NMSUtil;
import com.voxelgameslib.voxelgameslib.utils.ZipUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

/**
 * Handles the worlds (loading, unloading etc)
 */
@Singleton
public class WorldHandler implements Handler, Provider<WorldConfig> {

    private static final Logger log = Logger.getLogger(WorldHandler.class.getName());
    @Inject
    @Named("WorldsFolder")
    private File worldsFolder;
    @Inject
    @Named("WorldContainer")
    private File worldContainer;
    @Inject
    private ConfigHandler configHandler;
    @Inject
    private Gson gson;
    @Inject
    private MapScanner mapScanner;
    @Inject
    private WorldRepository worldRepository;
    @Inject
    private MapHandler mapHandler;

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
            Optional<MapInfo> mapInfo = getMapInfo(name);
            if (!mapInfo.isPresent()) {
                throw new MapException(
                        "Unknown map " + name + ". Did you register it into the world config?");
            }

            try {
                ZipFile zipFile = new ZipFile(new File(worldsFolder, mapInfo.get().getWorldName() + ".zip"));
                for (FileHeader header : (List<FileHeader>) zipFile.getFileHeaders()) {
                    if (header.getFileName().endsWith("config.json")) {
                        InputStream stream = zipFile.getInputStream(header);
                        Map m = gson.fromJson(new JsonReader(new InputStreamReader(stream)), Map.class);
                        m.initMarkers(mapHandler);
                        maps.add(m);
                        return m;
                    }
                }
                throw new MapException("Could not load map config for map " + name
                        + ". Fileheader was null. Does it has a map.json?");

            } catch (Exception e) {
                throw new MapException("Error while trying to load map config " + name + "(" + mapInfo.get().getWorldName() + ".zip)", e);
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
    public Optional<MapInfo> getMapInfo(@Nonnull String name) {
        return config.maps.stream().filter(mapInfo -> mapInfo.getWorldName().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Loads a world. Needs to copy the file from the repo, unzip it and let the implementation load it <br><b>Always
     * needs to call super! Super needs to be called first (because it copies the world)</b>
     *
     * @param map    the map that should be loaded
     * @param gameid the id of the game this map belongs to
     * @return the loaded world
     * @throws WorldException something goes wrong
     */
    @Nonnull
    public World loadWorld(@Nonnull Map map, @Nonnull UUID gameid, boolean replaceMarkers) {
        map.load(gameid, "TEMP_" + map.getWorldName() + "_" + gameid.toString().split("-")[0]);
        log.finer("Loading map " + map.getInfo().getDisplayName() + " as " + map.getLoadedName(gameid));

        File file = new File(worldContainer, map.getLoadedName(gameid));

        try {
            ZipFile zip = new ZipFile(new File(worldsFolder, map.getWorldName() + ".zip"));
            zip.extractAll(file.getAbsolutePath());
            FileUtils.delete(new File(file, "uid.dat"));
        } catch (ZipException e) {
            throw new WorldException("Could not unzip world " + map.getInfo().getDisplayName() + " (" + map.getWorldName() + ".zip).", e);
        }

        World world = loadLocalWorld(map.getLoadedName(gameid));

        if (replaceMarkers) {
            replaceMarkers(world, map);
        }

        // load chunks based on markers
        int i = 0;
        int a = 0;
        for (Marker m : map.getMarkers()) {
            Location l = m.getLoc().toLocation(world.getName());
            if (!l.getChunk().isLoaded() || !l.getWorld().isChunkLoaded(l.getChunk())) {
                l.getChunk().load();
                l.getWorld().loadChunk(l.getChunk());
                i++;
            } else {
                a++;
            }
        }

        log.finer("Loaded " + i + " chunks and ignored " + a + " already loaded");

        return world;
    }

    /**
     * Unloads a world. Needs to lets the implementation unload the world and delete the folder <br><b>Always needs to
     * call super! Super needs to be called last (because it deletes the world folder)</b>
     *
     * @param map    the map that should be unloaded.
     * @param gameid the id of the game that this map belongs to
     */
    public void unloadWorld(@Nonnull Map map, @Nonnull UUID gameid) {
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
    public void replaceMarkers(@Nonnull World world, @Nonnull Map map) {
        map.getMarkers().forEach(marker -> marker.getLoc().toLocation(world.getName()).getBlock().setType(Material.AIR));
        log.finer("Replaced " + map.getMarkers().size() + " markers with air");
        //TODO chest markers?
    }

    @Override
    public void enable() {
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
    public void disable() {
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
    @Nonnull
    public World loadLocalWorld(@Nonnull String name) {
        log.finer("Loading world " + name);
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
        log.finer("Unloading world " + name);
        Bukkit.unloadWorld(name, false);
    }

    /**
     * @return the folder where the playerable worlds are saved in (think about it as a repo for worlds/maps)
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

    public void finishWorldEditing(@Nonnull User editor, @Nonnull Map map) {
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
            zip = ZipUtil.createZip(worldFolder, map.getWorldName());
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

    public File getWorldContainer() {
        return this.worldContainer;
    }

    public WorldRepository getWorldRepository() {
        return this.worldRepository;
    }
}
