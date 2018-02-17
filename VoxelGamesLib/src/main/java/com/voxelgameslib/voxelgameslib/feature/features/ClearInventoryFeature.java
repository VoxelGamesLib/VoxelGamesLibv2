package com.voxelgameslib.voxelgameslib.feature.features;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

@FeatureInfo(name = "ClearInventoryFeature", author = "MiniDigger", version = "1.0",
        description = "Simple feature that clears the inventory of all players when the game starts (or a new player joins)")
public class ClearInventoryFeature extends AbstractFeature {

    // TODO add ability to restore the inv

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getInventory().clear());
    }

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent event) {
        event.getUser().getPlayer().getInventory().clear();
    }
}
