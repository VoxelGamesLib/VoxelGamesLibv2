package me.minidigger.voxelgameslib.feature.features;

import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.feature.FeatureInfo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@FeatureInfo(name = "NoDamageFeature", author = "MiniDigger", version = "1.0",
        description = "Small feature that disables any damage")
public class NoDamageFeature extends AbstractFeature {

    @Override
    public void start() {

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

    @SuppressWarnings("JavaDoc")
    @EventHandler
    public void onDmg(EntityDamageEvent event) {
        if (getPhase().getGame().isPlaying(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
