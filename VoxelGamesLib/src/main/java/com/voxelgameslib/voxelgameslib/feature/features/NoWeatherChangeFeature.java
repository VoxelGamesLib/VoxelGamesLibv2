package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

@FeatureInfo(name = "NoWeatherChangeFeature", author = "MiniDigger", version = "1.0", description = "Locks the weather")
public class NoWeatherChangeFeature extends AbstractFeature implements Listener {
    @Expose
    private WeatherType weather = WeatherType.CLEAR;

    private World world;

    public void setWeather(WeatherType weather) {
        this.weather = weather;
    }

    @Override
    public void enable() {
        world = getPhase().getFeature(MapFeature.class).getWorld();
        switch (weather) {
            case CLEAR:
                world.setStorm(false);
                world.setThundering(false);
                break;
            case DOWNFALL:
                world.setStorm(true);
                world.setThundering(true);
                break;
        }
    }

    @Nonnull
    @Override
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.singletonList(MapFeature.class);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.getWorld().getName().equals(world.getName())) {
            e.setCancelled(true);
        }
    }
}
