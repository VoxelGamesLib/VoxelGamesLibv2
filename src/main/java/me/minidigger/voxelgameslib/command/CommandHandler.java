package me.minidigger.voxelgameslib.command;

import javax.inject.Singleton;

import me.minidigger.voxelgameslib.feature.AbstractFeatureCommand;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.phase.Phase;

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
