package me.minidigger.voxelgameslib.metrics;

import org.bstats.Metrics;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.handler.Handler;

import lombok.extern.java.Log;

@Log
@Singleton
public class MetricHandler implements Handler {

    private Metrics metrics;

    @Inject
    private VoxelGamesLib voxelGamesLib;

    @Override
    public void start() {
        try {
            metrics = new Metrics(voxelGamesLib);
            //TODO add custom charts, like user/gamemode, installed gamesmodes, user/lang, installed langs etc
        } catch (Exception ex) {
            log.warning("Metrics failed to enabled. This is not a critical problem. You can ignore it.");
        }
    }

    @Override
    public void stop() {
        metrics = null;
    }
}
