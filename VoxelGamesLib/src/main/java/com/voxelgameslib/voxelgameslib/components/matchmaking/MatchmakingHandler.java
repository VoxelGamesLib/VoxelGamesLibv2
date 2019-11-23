package com.voxelgameslib.voxelgameslib.components.matchmaking;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.api.game.GameMode;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;

@Singleton
public class MatchmakingHandler implements Handler {

    private Map<GameMode, Queue> queues;

    @Override
    public void enable() {
        queues = new HashMap<>();
        //TODO match making
    }

    @Override
    public void disable() {
        queues.clear();
        queues = null;
    }
}
