package com.voxelgameslib.voxelgameslib.internal.texture;

import com.google.gson.Gson;
import com.google.inject.name.Named;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import org.mineskin.MineskinClient;
import org.mineskin.Model;
import org.mineskin.SkinOptions;
import org.mineskin.Visibility;
import org.mineskin.data.Skin;
import org.mineskin.data.SkinCallback;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;
import com.voxelgameslib.voxelgameslib.util.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@Singleton
public class TextureHandler implements Handler, Listener {

    private static final Logger log = Logger.getLogger(TextureHandler.class.getName());
    private List<Skin> loadedSkins = new ArrayList<>();
    private PlayerProfile errorProfile;

    @Inject
    @Named("IgnoreExposedBS")
    private Gson gson;

    @Inject
    @Named("SkinsFolder")
    private File skinsFolder;

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private TextureCache cache;

    private MineskinClient mineskinClient = new MineskinClient();

    @Override
    public void enable() {
        if (!skinsFolder.exists()) {
            log.warning("Skins folder doesn't exit, creating");
            skinsFolder.mkdirs();
        }
        File[] files = skinsFolder.listFiles();
        if (files != null) {
            Arrays.stream(files).map(file -> file.getName().replace(".json", "")).forEach(this::loadSkin);
        }
        log.info("Loaded " + loadedSkins.size() + " skins");

        fetchSkin(118300, null);

        // loading skins 0-9A-Z from provided ids
        try {
            InputStreamReader reader = new InputStreamReader(voxelGamesLib.getResource("ids.json"));
            //noinspection unchecked
            Map<String, Double> ids = gson.fromJson(reader, Map.class);
            ids.forEach((key, value) -> fetchSkin(value.intValue(), key, null));
            reader.close();
        } catch (IOException e) {
            log.warning("Error while loading skins, some features of VGL might not work as expected!");
            e.printStackTrace();
        }

        cache.init();

        // setup error profile
        errorProfile = Bukkit.createProfile("MHF_Question");
        VoxelGamesLib.newChain().async(() -> cache.fill(errorProfile)).execute();
    }

    @Override
    public void disable() {
        cache.persist();
    }

    @Nonnull
    public Optional<Skin> getSkin(@Nonnull String name) {
        return loadedSkins.stream().filter(skin -> skin.name.equals(name)).findFirst();
    }

    @Nonnull
    public Optional<Skin> getSkin(int id) {
        return loadedSkins.stream().filter(skin -> skin.id == id).findFirst();
    }

    public void fetchSkin(@Nonnull String name, @Nonnull String url, @Nullable SkinCallback skinCallback) {
        // check cache
        Optional<Skin> s = getSkin(name);
        if (s.isPresent()) {
            if (skinCallback != null) {
                skinCallback.done(s.get());
            }
            return;
        }

        // fetch from mineskin
        mineskinClient.generateUrl(url, SkinOptions.create(name, Model.DEFAULT, Visibility.PRIVATE), new SkinCallback() {
            @Override
            public void done(Skin skin) {
                loadedSkins.add(skin);
                TextureHandler.this.saveSkin(skin);
                if (skinCallback != null) {
                    skinCallback.done(skin);
                }
            }

            @Override
            public void error(String errorMessage) {
                log.warning("Error while fetching skin " + name + " (" + url + "): " + errorMessage);
            }

            @Override
            public void exception(Exception exception) {
                log.log(Level.WARNING, "Error while fetching skin " + name + " (" + url + "): ", exception);
            }
        });
    }

    public void fetchSkin(int id, @Nullable SkinCallback skinCallback) {
        fetchSkin(id, id + "", skinCallback);
    }

    public void fetchSkin(int id, String fallBackName, @Nullable SkinCallback skinCallback) {
        // check cache
        Optional<Skin> s = getSkin(id);
        if (s.isPresent()) {
            if (skinCallback != null) {
                skinCallback.done(s.get());
            }
            return;
        }

        // fetch from mineskin
        mineskinClient.getSkin(id, new SkinCallback() {
            @Override
            public void done(Skin skin) {
                if (skin.name.equals("")) {
                    skin.name = fallBackName;
                }
                loadedSkins.add(skin);
                TextureHandler.this.saveSkin(skin);
                if (skinCallback != null) {
                    skinCallback.done(skin);
                }
            }

            @Override
            public void error(String errorMessage) {
                log.warning("Error while fetching skin #" + id + " (" + fallBackName + "): " + errorMessage);
            }

            @Override
            public void exception(Exception exception) {
                log.log(Level.WARNING, "Error while fetching skin #" + id + " (" + fallBackName + "): ", exception);
            }
        });
    }

    public void saveSkin(@Nonnull Skin skin) {
        try {
            if (skin.name.equals(""))
                throw new IllegalArgumentException("Skin has to have a name!");
            File file = new File(skinsFolder, skin.name + ".json");
            if (file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file);
            gson.toJson(skin, fw);
            fw.close();
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error while saving skin " + skin.name, ex);
        }
    }

    @Nullable
    public Skin loadSkin(@Nonnull String name) {
        try {
            FileReader fr = new FileReader(new File(skinsFolder, name + ".json"));
            Skin skin = gson.fromJson(fr, Skin.class);
            fr.close();
            loadedSkins.add(skin);
            return skin;
        } catch (Exception ex) {
            log.log(Level.WARNING, "could not load skin " + name, ex);
        }

        return null;
    }

    public PlayerProfile getPlayerProfile(Skin skin) {
        PlayerProfile playerProfile = Bukkit.createProfile(skin.data.uuid, skin.name);
        playerProfile.setProperty(new ProfileProperty("textures", skin.data.texture.value, skin.data.texture.signature));
        return playerProfile;
    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        return cache.get(uuid);
    }

    public PlayerProfile getPlayerProfile(String owner) {
        Optional<Skin> skin = getSkin(owner);
        if (skin.isPresent()) {
            return getPlayerProfile(skin.get());
        } else {
            return cache.get(owner);
        }
    }

    @Nullable
    public ItemStack getSkull(@Nonnull Skin skin) {
        return new ItemBuilder(Material.PLAYER_HEAD).name(skin.name).meta((itemMeta -> ((SkullMeta) itemMeta).setPlayerProfile(getPlayerProfile(skin)))).build();
    }

    public PlayerProfile getErrorProfile() {
        return errorProfile;
    }
}
