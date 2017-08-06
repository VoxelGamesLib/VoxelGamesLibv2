package com.voxelgameslib.voxelgameslib.texture;

import com.google.gson.Gson;
import com.google.inject.name.Named;

import com.voxelgameslib.voxelgameslib.handler.Handler;

import org.mineskin.MineskinClient;
import org.mineskin.data.Skin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.SkullMeta;

import lombok.extern.java.Log;

@Log
@Singleton
public class TextureHandler implements Handler {

    private List<Skin> loadedSkins = new ArrayList<>();

    @Inject
    @Named("IgnoreExposedBS")
    private Gson gson;

    @Inject
    @Named("SkinsFolder")
    private File skinsFolder;

    private MineskinClient mineskinClient = new MineskinClient();

    @Override
    public void start() {
        File[] files = skinsFolder.listFiles();
        if (files != null) {
            Arrays.stream(files).map(file -> file.getName().replace(".json", "")).forEach(this::loadSkin);
        }
        log.info("Loaded " + loadedSkins.size() + " skins");
        fetchSkin(29279);
    }

    @Override
    public void stop() {

    }

    public void fetchSkin(String url) {
        mineskinClient.generateUrl(url, skin -> {
            loadedSkins.add(skin);
            saveSkin(skin);
        });
    }

    public void fetchSkin(int id) {
        mineskinClient.getSkin(id, skin -> {
            loadedSkins.add(skin);
            saveSkin(skin);
        });
    }

    public void saveSkin(Skin skin) {
        try {
            FileWriter fw = new FileWriter(new File(skinsFolder, skin.name));
            gson.toJson(skin, fw);
            fw.close();
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error while saving skin " + skin.name, ex);
        }
    }

    public Skin loadSkin(String name) {
        try {
            FileReader fr = new FileReader(new File(skinsFolder, name));
            Skin skin = gson.fromJson(fr, Skin.class);
            fr.close();
            loadedSkins.add(skin);
            return skin;
        } catch (Exception ex) {
            log.log(Level.WARNING, "could not load skin " + name, ex);
        }

        return null;
    }

    public Skull getSkull(Skin skin) {

    }
}
