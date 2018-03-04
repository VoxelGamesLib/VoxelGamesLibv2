package com.voxelgameslib.voxelgameslib.stats.types;

import com.voxelgameslib.voxelgameslib.stats.Stat;
import com.voxelgameslib.voxelgameslib.stats.StatType;

import org.bukkit.Bukkit;

public class PlayTimeStat extends Stat {

    @Override
    public boolean shouldTick() {
        return true;
    }

    @Override
    public void tickOneMinute() {
        Bukkit.getOnlinePlayers().forEach((p) -> getInstance(p.getUniqueId()).increment());
    }

    @Override
    public StatType getType() {
        return StatType.PLAY_TIME;
    }
}
