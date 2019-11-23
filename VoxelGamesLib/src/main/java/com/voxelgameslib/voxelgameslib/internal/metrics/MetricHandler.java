package com.voxelgameslib.voxelgameslib.internal.metrics;

import org.bstats.bukkit.Metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.api.game.GameHandler;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;

@Singleton
public class MetricHandler implements Handler {

    private static final Logger log = Logger.getLogger(MetricHandler.class.getName());
    private Metrics metrics;

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private GameHandler gameHandler;

    @Override
    public void enable() {
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
    public void disable() {
        metrics = null;
    }
}
