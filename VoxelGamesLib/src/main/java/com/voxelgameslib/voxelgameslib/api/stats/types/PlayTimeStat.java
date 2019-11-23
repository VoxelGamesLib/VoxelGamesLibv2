package com.voxelgameslib.voxelgameslib.api.stats.types;

import com.voxelgameslib.voxelgameslib.api.stats.Stat;
import com.voxelgameslib.voxelgameslib.api.stats.StatType;
import com.voxelgameslib.voxelgameslib.api.stats.Trackable;

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
