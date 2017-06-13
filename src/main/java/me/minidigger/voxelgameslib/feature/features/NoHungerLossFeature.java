package me.minidigger.voxelgameslib.feature.features;

import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.feature.FeatureInfo;
import me.minidigger.voxelgameslib.user.User;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@FeatureInfo(name = "NoHungerLossFeature", author = "aphelion", version = "1.0",
        description = "Stops hunger loss")
public class NoHungerLossFeature extends AbstractFeature {

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(this::feed);
    }

    public void feed(User user) {
        user.getPlayer().setFoodLevel(20);
    }

    @EventHandler
    public void onHungerDepletion(FoodLevelChangeEvent event) {
        event.setCancelled(true);
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
