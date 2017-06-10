package me.minidigger.voxelgameslib.scoreboard;

import me.minidigger.voxelgameslib.handler.Handler;
import org.bukkit.Bukkit;

public class ScoreboardHandler implements Handler {

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  public Scoreboard createScoreboard(String name) {
    BukkitScoreboard scoreboard = new BukkitScoreboard();
    scoreboard.setTitle(name);
    scoreboard.setImplObject(Bukkit.getScoreboardManager().getNewScoreboard());
    return scoreboard;
  }
}
