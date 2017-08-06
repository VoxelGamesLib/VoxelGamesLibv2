package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import javax.annotation.Nonnull;

import org.bukkit.event.entity.EntityDamageEvent;

@FeatureInfo(name = "NoDamageFeature", author = "MiniDigger", version = "1.0",
        description = "Small feature that disables any damage")
public class NoDamageFeature extends AbstractFeature {

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onDmg(@Nonnull EntityDamageEvent event) {
        event.setCancelled(true);
    }
}
