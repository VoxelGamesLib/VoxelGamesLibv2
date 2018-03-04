package com.voxelgameslib.voxelgameslib.stats.types;

import com.voxelgameslib.voxelgameslib.stats.Stat;

import org.bukkit.Bukkit;

public class PlayTimeStat extends Stat {
    @Override
    public String getName() {
        return "PlayTime";
    }

    @Override
    public boolean shouldTick() {
        return true;
    }

    @Override
    public void tickOneMinute() {
        Bukkit.getOnlinePlayers().forEach((p) -> getInstance(p.getUniqueId()).increment());
    }
}
