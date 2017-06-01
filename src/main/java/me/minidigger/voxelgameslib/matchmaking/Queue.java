package me.minidigger.voxelgameslib.matchmaking;

import lombok.Data;
import me.minidigger.voxelgameslib.game.GameMode;

/**
 * Created by Martin on 04.02.2017.
 */
@Data
public class Queue {

  private GameMode gameMode;
  private boolean ranked;
}
