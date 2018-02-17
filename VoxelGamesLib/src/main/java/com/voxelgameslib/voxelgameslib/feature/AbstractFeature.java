package com.voxelgameslib.voxelgameslib.feature;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.phase.Phase;

import org.bukkit.event.Listener;

/**
 * Abstract implementation of Phase
 */
public abstract class AbstractFeature implements Feature, Listener {

    @Expose
    private String name;

    private Phase phase;

    /**
     * Sets the name of this feature to the name of the class
     */
    public AbstractFeature() {
        name = getClass().getName().replace(FeatureTypeAdapter.DEFAULT_PATH + ".", "");
    }

    @Nonnull
    @Override
    public Phase getPhase() {
        return phase;
    }

    @Override
    public void setPhase(@Nonnull Phase phase) {
        this.phase = phase;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }
}
