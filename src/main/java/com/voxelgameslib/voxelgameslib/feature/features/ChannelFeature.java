package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import javax.annotation.Nonnull;

/**
 * This feature allows the ability for multiple channels in phases, e.g. for team based chats. It
 * simply saves you the headache of doing it all manually.
 */
@FeatureInfo(name = "ChannelFeature", author = "aphel", version = "1.0",
        description = "Adds support for multiple channels in phases, e.g. for team based chats")
public class ChannelFeature extends AbstractFeature {

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
    @Nonnull
    public Class[] getDependencies() {
        return new Class[0];
    }
}
