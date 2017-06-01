package me.minidigger.voxelgameslib.phase;

import com.google.gson.annotations.Expose;
import javax.inject.Inject;
import me.minidigger.voxelgameslib.bossbar.BossBar;
import me.minidigger.voxelgameslib.bossbar.BossBarColor;
import me.minidigger.voxelgameslib.bossbar.BossBarStyle;
import me.minidigger.voxelgameslib.server.Server;

/**
 * A special {@link Phase} that automatically ends after a specified amount of ticks.
 */
public abstract class TimedPhase extends AbstractPhase {

  @Expose
  private int ticks;

  @Inject
  private Server server;

  private double originalTicks;
  private BossBar bossBar;
  private boolean started;

  /**
   * Sets the amount of ticks this phase should tick, can be modified after start
   *
   * @param ticks the amount of ticks this phase should tick
   */
  public void setTicks(int ticks) {
    this.ticks = ticks;
    this.originalTicks = ticks;
  }

  /**
   * @return the amount of ticks this phase will go on for
   */
  public int getTicks() {
    return ticks;
  }

  @Override
  public void start() {
    super.start();

    originalTicks = ticks;

    bossBar = server.createBossBar(getName(), BossBarColor.BLUE, BossBarStyle.SPLIT_20);

    getGame().getPlayers().forEach(u -> bossBar.addUser(u));
    getGame().getSpectators().forEach(u -> bossBar.addUser(u));

    started = true;
  }

  @Override
  public void stop() {
    super.stop();

    if (started) {
      bossBar.removeAll();
    }
  }

  @Override
  public void tick() {
    super.tick();
    ticks--;

    if (ticks <= 0) {
      getGame().endPhase();
    } else {
      bossBar.setProgress((ticks / originalTicks));
    }
  }
}
