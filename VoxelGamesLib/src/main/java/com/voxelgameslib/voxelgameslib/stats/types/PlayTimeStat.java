package com.voxelgameslib.voxelgameslib.stats.types;

import com.voxelgameslib.voxelgameslib.stats.Stat;
import com.voxelgameslib.voxelgameslib.stats.StatType;
import com.voxelgameslib.voxelgameslib.stats.Trackable;

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
    public Trackable getType() {
        return StatType.PLAY_TIME;
    }
}
