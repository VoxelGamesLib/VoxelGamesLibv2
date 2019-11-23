package com.voxelgameslib.voxelgameslib.api.feature.features;

import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;

public class RemovePotionEffectsFeature extends AbstractFeature {

    @Override
    public void enable() {
        getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getActivePotionEffects()
                .forEach((potionEffect -> user.getPlayer().removePotionEffect(potionEffect.getType()))));
    }
}
