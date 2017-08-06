package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameLeaveEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

/**
 * Provides a boss bar instance for other features
 */
public class BossBarFeature extends AbstractFeature {

    @Expose
    private String message = "";
    @Expose
    private BarColor color = BarColor.BLUE;
    @Expose
    private BarStyle style = BarStyle.SEGMENTED_20;

    private BossBar bossBar;

    /**
     * @return the bossbar that will be used for this phase
     */
    @Nonnull
    public BossBar getBossBar() {
        return bossBar;
    }

    @Override
    public void start() {
        bossBar = Bukkit.createBossBar(message, color, style);

        getPhase().getGame().getPlayers().forEach(user -> bossBar.addPlayer(user.getPlayer()));
    }

    @Override
    public void stop() {
        bossBar.removeAll();
    }

    @GameEvent
    public void onGameJoin(@Nonnull GameJoinEvent event) {
        bossBar.addPlayer(event.getUser().getPlayer());
    }

    @GameEvent
    public void onGameLeave(@Nonnull GameLeaveEvent event) {
        bossBar.removePlayer(event.getUser().getPlayer());
    }
}
