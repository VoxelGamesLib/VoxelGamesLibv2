package me.minidigger.voxelgameslib.game;

import com.google.gson.annotations.Expose;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Data;
import me.minidigger.voxelgameslib.phase.Phase;

/**
 * Holds all important information of a game, ready to be saved to and loaded from json
 */
@Data
public class GameDefinition {

  @Expose
  @Nonnull
  private GameMode gameMode;

  @Expose
  private int minPlayers;

  @Expose
  private int maxPlayers;

  @Expose
  @Nonnull
  private List<Phase> phases;

  @Expose
  @Nonnull
  private Map<String, Object> gameData;
}
