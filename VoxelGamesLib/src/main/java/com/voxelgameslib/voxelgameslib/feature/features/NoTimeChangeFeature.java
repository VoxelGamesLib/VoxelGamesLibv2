package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import org.bukkit.World;

@FeatureInfo(name = "NoTimeChangeFeature", author = "MiniDigger", version = "1.0", description = "Locks the time")
public class NoTimeChangeFeature extends AbstractFeature {

    @Expose
    private long time = 6000;

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void enable() {
        World w = getPhase().getFeature(MapFeature.class).getWorld();
        w.setTime(time);
        w.setGameRuleValue("doDaylightCycle", "false");
    }

    @Nonnull
    @Override
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.singletonList(MapFeature.class);
    }
}
