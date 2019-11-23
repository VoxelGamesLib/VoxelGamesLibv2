package com.voxelgameslib.voxelgameslib.api.feature.features;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;

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
