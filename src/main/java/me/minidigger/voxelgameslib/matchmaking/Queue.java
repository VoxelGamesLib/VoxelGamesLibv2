package me.minidigger.voxelgameslib.matchmaking;

import lombok.Data;
import me.minidigger.voxelgameslib.game.GameMode;

@Data
public class Queue {

  private GameMode gameMode;
  private boolean ranked;
}
