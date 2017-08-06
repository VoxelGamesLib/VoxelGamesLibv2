package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;

import javax.annotation.Nonnull;

public class RemovePotionEffectsFeature extends AbstractFeature {

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getActivePotionEffects()
                .forEach((potionEffect -> user.getPlayer().removePotionEffect(potionEffect.getType()))));
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
    @Nonnull
    public Class[] getDependencies() {
        return new Class[0];
    }
}
