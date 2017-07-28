package me.minidigger.voxelgameslib.metrics;

import org.bstats.bukkit.Metrics;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.handler.Handler;

import lombok.extern.java.Log;

@Log
@Singleton
public class MetricHandler implements Handler {

    private Metrics metrics;

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private GameHandler gameHandler;

    @Override
    public void start() {
        try {
            metrics = new Metrics(voxelGamesLib);
            //TODO add custom charts, like user/gamemode, installed gamesmodes, user/lang, installed langs etc

            // gamemodes multiline TODO enable this on the bstats page once its implemented....
            metrics.addCustomChart(new Metrics.MultiLineChart("gamemodes", () -> {
                Map<String, Integer> valueMap = new HashMap<>();
                gameHandler.getGameModes().forEach((gm) -> valueMap.put(gm.getName(), 1));
                return valueMap;
            }));
        } catch (Throwable ex) {
            log.warning("Metrics failed to enabled. This is not a critical problem. You can ignore it.");
        }
    }

    @Override
    public void stop() {
        metrics = null;
    }
}
