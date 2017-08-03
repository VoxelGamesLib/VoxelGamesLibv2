package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.user.User;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@FeatureInfo(name = "NoHungerLossFeature", author = "aphelion", version = "1.0",
        description = "Stops hunger loss")
public class NoHungerLossFeature extends AbstractFeature {

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(this::feed);
    }

    public void feed(User user) {
        user.getPlayer().setFoodLevel(20);
    }

    @GameEvent
    public void onHungerDepletion(FoodLevelChangeEvent event) {
        event.setCancelled(true);
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
}
