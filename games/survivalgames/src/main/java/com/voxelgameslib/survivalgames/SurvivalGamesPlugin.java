package com.voxelgameslib.survivalgames;

import com.google.inject.Singleton;

import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.module.Module;
import com.voxelgameslib.voxelgameslib.module.ModuleHandler;
import com.voxelgameslib.voxelgameslib.module.ModuleInfo;

import org.bukkit.plugin.java.JavaPlugin;

@Singleton
@ModuleInfo(name = "SurvivalGames", authors = "MiniDigger", version = "1.0.0")
public class SurvivalGamesPlugin extends JavaPlugin implements Module {

    public static final GameMode GAMEMODE = new GameMode("SurvivalGames", SurvivalGamesGame.class);

    @Inject
    private GameHandler gameHandler;

    @Override
    public void onLoad() {
        ModuleHandler.offerModule(this); // always do this first!
    }

    @Override
    public void enable() {
        gameHandler.registerGameMode(GAMEMODE);
    }

    @Override
    public void disable() {

    }
}
