package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

@FeatureInfo(name = "ClearInventoryFeature", author = "MiniDigger", version = "1.0",
        description = "Simple feature that clears the inventory of all players when the game starts (or a new player joins)")
public class ClearInventoryFeature extends AbstractFeature {

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getInventory().clear());
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

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onJoin(GameJoinEvent event) {
        event.getUser().getPlayer().getInventory().clear();
    }
}
