package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import org.bukkit.GameMode;

@FeatureInfo(name = "GameModeFeature", author = "MiniDigger", version = "1.0",
        description = "Simple feature that changes the gamemode of all players in the phase")
public class GameModeFeature extends AbstractFeature {

    @Expose
    private GameMode mode = GameMode.SURVIVAL;

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(u -> u.getPlayer().setGameMode(mode));
    }

    @GameEvent
    public void onJoin(GameJoinEvent event) {
        event.getUser().getPlayer().setGameMode(mode);
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {

    }

    @Override
    public Class[] getDependencies() {
        return new Class[0];
    }

    /**
     * Sets the gamemode for this phase
     *
     * @param gameMode the new gamemode
     */
    public void setGameMode(GameMode gameMode) {
        this.mode = gameMode;
    }
}
