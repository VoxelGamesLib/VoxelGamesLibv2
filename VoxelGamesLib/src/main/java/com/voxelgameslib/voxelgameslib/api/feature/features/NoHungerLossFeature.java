package com.voxelgameslib.voxelgameslib.api.feature.features;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.components.user.User;

import org.bukkit.event.entity.FoodLevelChangeEvent;

@FeatureInfo(name = "NoHungerLossFeature", author = "aphelion", version = "1.0",
        description = "Stops hunger loss")
public class NoHungerLossFeature extends AbstractFeature {

    @Override
    public void enable() {
        getPhase().getGame().getPlayers().forEach(this::feed);
    }

    public void feed(@Nonnull User user) {
        user.getPlayer().setFoodLevel(20);
    }

    @GameEvent
    public void onHungerDepletion(@Nonnull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
