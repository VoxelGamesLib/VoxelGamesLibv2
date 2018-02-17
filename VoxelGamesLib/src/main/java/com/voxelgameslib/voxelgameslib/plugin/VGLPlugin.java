package com.voxelgameslib.voxelgameslib.plugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.module.Module;
import com.voxelgameslib.voxelgameslib.module.ModuleHandler;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Who doesn't like a good template?
 */
@Singleton
public abstract class VGLPlugin extends JavaPlugin implements Module {

    @Inject
    protected GameHandler gameHandler;


    @Override
    public void onLoad() {
        ModuleHandler.offerModule(this);
    }

    @Override
    public void enable() {
        gameHandler.registerGameMode(getGameMode());
    }

    @Override
    public void disable() {

    }

    protected abstract GameMode getGameMode();

}
