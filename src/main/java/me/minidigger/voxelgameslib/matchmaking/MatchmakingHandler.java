package me.minidigger.voxelgameslib.matchmaking;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.handler.Handler;

/**
 * Created by Martin on 04.02.2017.
 */
@Singleton
public class MatchmakingHandler implements Handler {

  private Map<GameMode, Queue> queues;

  @Override
  public void start() {
    queues = new HashMap<>();
    //TODO match making
  }

  @Override
  public void stop() {
    queues.clear();
    queues = null;
  }
}
