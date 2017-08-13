package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
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
    public void onJoin(@Nonnull GameJoinEvent event) {
        event.getUser().getPlayer().setGameMode(mode);
    }

    /**
     * Sets the gamemode for this phase
     *
     * @param gameMode the new gamemode
     */
    public void setGameMode(@Nonnull GameMode gameMode) {
        this.mode = gameMode;
    }
}
