package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;

import javax.annotation.Nonnull;

import org.bukkit.World;

public class TimeFeature extends AbstractFeature {

    @Expose
    private boolean shouldChange = false;
    @Expose
    private int time = 6000;

    @Override
    public void start() {
        World world = getPhase().getFeature(MapFeature.class).getWorld();
        world.setGameRuleValue("doDaylightCycle", shouldChange + "");
        world.setTime(time);
    }

    @Nonnull
    @Override
    public Class[] getDependencies() {
        return new Class[]{MapFeature.class};
    }
}
