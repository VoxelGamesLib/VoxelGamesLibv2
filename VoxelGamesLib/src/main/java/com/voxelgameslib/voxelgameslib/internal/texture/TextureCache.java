package com.voxelgameslib.voxelgameslib.internal.texture;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.inject.name.Named;

import com.bugsnag.Severity;
import com.destroystokyo.paper.event.profile.PreFillProfileEvent;
import com.destroystokyo.paper.profile.PlayerProfile;

import org.apache.commons.lang3.StringUtils;
import org.mineskin.data.Skin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.components.placeholders.SkullPlaceHolder;
import com.voxelgameslib.voxelgameslib.components.placeholders.SkullPlaceHolders;
import com.voxelgameslib.voxelgameslib.internal.error.ErrorHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Singleton
public class TextureCache implements Listener {

    private File uuidFile;
    private File nameFile;

    private LoadingCache<UUID, PlayerProfile> uuidCache;
    private LoadingCache<String, PlayerProfile> nameCache;

    private Set<UUID> currentUUIDs = Sets.newConcurrentHashSet();
    private Set<String> currentNames = Sets.newConcurrentHashSet();

    private static final Logger log = Logger.getLogger(TextureCache.class.getName());

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    @Named("IgnoreExposedBS")
    private Gson gson;
    @Inject
    private SkullPlaceHolders skullPlaceHolders;
    @Inject
    private TextureHandler textureHandler;
    @Inject
    private ErrorHandler errorHandler;

    public void init() {
        uuidCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterAccess(1, TimeUnit.DAYS)
                .build(new CacheLoader<UUID, PlayerProfile>() {
                    @Override
                    public PlayerProfile load(UUID key) {
                        PlayerProfile playerProfile = Bukkit.createProfile(key);
                        //playerProfile.completeFromCache();
                        if (!playerProfile.hasTextures()) {
                            playerProfile.complete(true);
                        }
//                    if (!playerProfile.hasTextures()) {
//                        MinecraftServer server = MinecraftServer.getServer();
//                        // if we are in offline mode, lets access mojang anyways because why not?!
//                        boolean isOnlineMode = server.getOnlineMode() || (SpigotConfig.bungee && PaperConfig.bungeeOnlineMode);
//                        if (!isOnlineMode) {
//                            GameProfile gameProfile = CraftPlayerProfile.asAuthlib(playerProfile);
//                            server.getSessionService().fillProfileProperties(gameProfile, true);
//                            return CraftPlayerProfile.asBukkitMirror(gameProfile);
//                        } else {
//                            log.finer("Something went wrong while fetching texture " + key);
//                        }//TODO fix this, easiest way would be to implement the network call myself I guess
//                    }
                        return playerProfile;
                    }
                });

        nameCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterAccess(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, PlayerProfile>() {
                    @Override
                    public PlayerProfile load(String key) {
                        PlayerProfile playerProfile = Bukkit.createProfile(key);
                        //playerProfile.completeFromCache();
                        if (!playerProfile.hasTextures()) {
                            playerProfile.complete(true);
                        }
                        return playerProfile;
                    }
                });

        Bukkit.getPluginManager().registerEvents(this, voxelGamesLib);

        File cacheFolder = new File(voxelGamesLib.getDataFolder(), "cache");
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir();
        }

        uuidFile = new File(cacheFolder, "uuid.json");
        nameFile = new File(cacheFolder, "name.json");

        try {
            // preload uuid cache
            if (uuidFile.exists()) {
                try (FileReader fr = new FileReader(uuidFile)) {
                    //noinspection unchecked
                    Map<UUID, PlayerProfile> map = gson.fromJson(fr, Map.class);
                    log.finer("Loaded " + map.size() + " entries for the uuid cache");
                    uuidCache.putAll(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // preload name cache
            if (nameFile.exists()) {
                try (FileReader fr = new FileReader(nameFile)) {
                    //noinspection unchecked
                    Map<String, PlayerProfile> map = gson.fromJson(fr, Map.class);
                    log.finer("Loaded " + map.size() + " entries for the name cache");
                    nameCache.putAll(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            errorHandler.handle(ex, Severity.WARNING, true);
        }
    }

    public void persist() {
        try (FileWriter fw = new FileWriter(uuidFile)) {
            Map<UUID, PlayerProfile> map = uuidCache.asMap();
            gson.toJson(map, fw);
            log.finer("Persisted " + uuidCache.asMap().size() + " entries for the uuid cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fw = new FileWriter(nameFile)) {
            gson.toJson(nameCache.asMap(), fw);
            log.finer("Persisted " + nameCache.asMap().size() + " entries for the name cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void fill(PreFillProfileEvent e) {
        // handle locks to avoid being called twice
        if (e.getPlayerProfile().getName() != null) {
            if (currentNames.contains(e.getPlayerProfile().getName())) {
                log.finer("Ignoring " + e.getPlayerProfile().getName() + " (" + e.getPlayerProfile().getId() + ")");
                return;
            } else {
                currentNames.add(e.getPlayerProfile().getName());
            }
        }
        if (e.getPlayerProfile().getId() != null) {
            if (currentUUIDs.contains(e.getPlayerProfile().getId())) {
                log.finer("Ignoring " + e.getPlayerProfile().getName() + " (" + e.getPlayerProfile().getId() + ")");
                return;
            } else {
                currentUUIDs.add(e.getPlayerProfile().getId());
            }
        }

        log.finer("Fill profile for " + e.getPlayerProfile().getName() + "(" + e.getPlayerProfile().getId() + ")");
        if (!fill(e.getPlayerProfile())) {
            log.finer("No success :/");
        }
    }

    public boolean fill(PlayerProfile playerProfile) {
        if (checkForPlaceholders(playerProfile)) {
            return true;
        }

        PlayerProfile prefixProfile = checkForPrefix(playerProfile);
        if (prefixProfile != null) {
            playerProfile.setProperties(prefixProfile.getProperties());
            return playerProfile.isComplete() && playerProfile.hasTextures();
        }

        PlayerProfile newProfile = null;
        //playerProfile.completeFromCache();
        if (playerProfile.getId() != null) {
            try {
                newProfile = uuidCache.get(playerProfile.getId());
                if (!newProfile.hasTextures()) {
                    uuidCache.invalidate(playerProfile.getId());
                }
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        } else if (!StringUtils.isBlank(playerProfile.getName())) {
            try {
                newProfile = nameCache.get(playerProfile.getName());
                if (!newProfile.hasTextures()) {
                    nameCache.invalidate(playerProfile.getName());
                }
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        }

        // remove locks
        if (playerProfile.getId() != null) {
            currentUUIDs.remove(playerProfile.getId());
        }
        if (playerProfile.getName() != null) {
            currentNames.remove(playerProfile.getName());
        }

        if (newProfile != null) {
            playerProfile.setProperties(newProfile.getProperties());
            return newProfile.isComplete() && newProfile.hasTextures();
        } else {
            return false;
        }
    }

    public PlayerProfile get(String name) {
        PlayerProfile playerProfile = Bukkit.createProfile(name);
        currentNames.add(name);
        fill(playerProfile);
        return playerProfile;
    }

    public PlayerProfile get(UUID id) {
        PlayerProfile playerProfile = Bukkit.createProfile(id);
        currentUUIDs.add(id);
        fill(playerProfile);
        return playerProfile;
    }

    private boolean checkForPlaceholders(PlayerProfile playerProfile) {
        if (playerProfile.getName() != null) {
            for (Map.Entry<String, SkullPlaceHolder> entry : skullPlaceHolders.getPlaceHolders().entrySet()) {
                if (playerProfile.getName().startsWith(entry.getKey())) {
                    PlayerProfile errorProfile = textureHandler.getErrorProfile();
                    playerProfile.setProperties(errorProfile.getProperties());
                    log.finer("Found placeholder trying to be filled, fill with error profile for now");
                    return true;
                }
            }
        }
        return false;
    }

    private PlayerProfile checkForPrefix(PlayerProfile playerProfile) {
        if (playerProfile.getName() != null) {
            if (playerProfile.getName().contains(":")) {
                String[] args = playerProfile.getName().split(":");
                Optional<Skin> skin = textureHandler.getSkin(args[1].charAt(0));
                if (skin.isPresent()) {
                    log.finer("Found prefix marker, return " + skin.get().name);
                    return textureHandler.getPlayerProfile(skin.get());
                } else {
                    log.warning("Requested prefix marker, but is missing skin! " + playerProfile.getName());
                    return textureHandler.getErrorProfile();
                }
            }
        }

        return null;
    }
}
