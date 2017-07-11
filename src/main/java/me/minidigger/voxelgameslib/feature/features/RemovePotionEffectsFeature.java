package me.minidigger.voxelgameslib.feature.features;

import me.minidigger.voxelgameslib.feature.AbstractFeature;

public class RemovePotionEffectsFeature extends AbstractFeature {
    
    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(user -> {
            user.getPlayer().getActivePotionEffects().forEach((potionEffect -> user.getPlayer().removePotionEffect(potionEffect.getType())));
        });
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
