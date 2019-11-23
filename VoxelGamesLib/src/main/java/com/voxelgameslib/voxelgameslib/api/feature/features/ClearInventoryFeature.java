package com.voxelgameslib.voxelgameslib.api.feature.features;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;

@FeatureInfo(name = "ClearInventoryFeature", author = "MiniDigger", version = "1.0",
        description = "Simple feature that clears the inventory of all players when the game starts (or a new player joins)")
public class ClearInventoryFeature extends AbstractFeature {

    // TODO add ability to restore the inv

    @Override
    public void enable() {
        getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getInventory().clear());
    }

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent event) {
        event.getUser().getPlayer().getInventory().clear();
    }
}
