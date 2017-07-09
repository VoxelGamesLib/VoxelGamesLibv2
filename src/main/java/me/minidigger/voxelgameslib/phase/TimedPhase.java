package me.minidigger.voxelgameslib.phase;

import com.google.gson.annotations.Expose;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import lombok.extern.java.Log;

/**
 * A special {@link Phase} that automatically ends after a specified amount of ticks.
 */
@Log
public abstract class TimedPhase extends AbstractPhase {

    @Expose
    private int ticks;

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

        log.finer("start timed phase with name " + getName());
        bossBar = Bukkit.createBossBar(getName(), BarColor.BLUE, BarStyle.SEGMENTED_20);

        getGame().getPlayers().forEach(u -> bossBar.addPlayer(u.getPlayer()));
        getGame().getSpectators().forEach(u -> bossBar.addPlayer(u.getPlayer()));

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
