package com.voxelgameslib.voxelgameslib.api.stats.types;

import javax.annotation.Nullable;

import com.voxelgameslib.voxelgameslib.api.stats.Stat;
import com.voxelgameslib.voxelgameslib.api.stats.StatType;
import com.voxelgameslib.voxelgameslib.api.stats.Trackable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinCountStat extends Stat {

    @Nullable
    @Override
    public Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent e) {
                getInstance(e.getPlayer().getUniqueId()).increment();
            }
        };
    }

    @Override
    public Trackable getType() {
        return StatType.JOIN_COUNT;
    }
}
