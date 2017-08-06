package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import javax.annotation.Nonnull;

import org.bukkit.event.entity.PlayerDeathEvent;

@FeatureInfo(name = "AutoRespawnFeature", author = "MiniDigger", version = "1.0",
        description = "Makes dead players respawn automatically")
public class AutoRespawnFeature extends AbstractFeature {

    @GameEvent
    public void onDeath(@Nonnull PlayerDeathEvent event) {
        event.getEntity().spigot().respawn();
    }
}
