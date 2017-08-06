package com.voxelgameslib.voxelgameslib.command;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeatureCommand;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.phase.Phase;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
public class CommandHandler implements Handler {

    @Override
    public void start() {
        System.out.println("TEST");
        System.out.println("test2");
    }

    @Override
    public void stop() {

    }

    public void register(@Nonnull AbstractFeatureCommand cmd, @Nonnull Phase phase) {
        //TODO bind commands to a feature
    }

    public void unregister(@Nonnull AbstractFeatureCommand cmd, @Nonnull Phase phase) {

    }
}
