package com.voxelgameslib.voxelgameslib.command;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeatureCommand;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.phase.Phase;

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

    public void register(AbstractFeatureCommand cmd, Phase phase) {

    }

    public void unregister(AbstractFeatureCommand cmd, Phase phase) {

    }
}
