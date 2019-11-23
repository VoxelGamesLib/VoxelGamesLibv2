package com.voxelgameslib.voxelgameslib.api.feature.features;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

@FeatureInfo(name = "BleedFeature", author = "MiniDigger", version = "1.0", description = "Displays a bleed effect around the player")
public class BleedFeature extends AbstractFeature {

    @GameEvent
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_WIRE);
            e.getEntity().playEffect(EntityEffect.HURT);
        }
    }
}
