package com.voxelgameslib.voxelgameslib.api.feature.features;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;

import org.bukkit.event.entity.PlayerDeathEvent;

@FeatureInfo(name = "AutoRespawnFeature", author = "MiniDigger", version = "1.0",
        description = "Makes dead players respawn automatically")
public class AutoRespawnFeature extends AbstractFeature {

    @GameEvent
    public void onDeath(@Nonnull PlayerDeathEvent event) {
        event.getEntity().spigot().respawn();
    }
}
