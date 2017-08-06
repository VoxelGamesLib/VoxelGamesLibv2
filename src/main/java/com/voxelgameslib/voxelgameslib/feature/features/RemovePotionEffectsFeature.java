package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;

public class RemovePotionEffectsFeature extends AbstractFeature {

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getActivePotionEffects()
                .forEach((potionEffect -> user.getPlayer().removePotionEffect(potionEffect.getType()))));
    }
}
