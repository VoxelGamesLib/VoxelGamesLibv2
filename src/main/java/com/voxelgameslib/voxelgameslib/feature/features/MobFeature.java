package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;

import java.util.Arrays;
import javax.annotation.Nonnull;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobFeature extends AbstractFeature {

    @Expose
    private EntityType[] whitelist = new EntityType[0];
    @Expose
    private EntityType[] blacklist = new EntityType[0];
    @Expose
    private boolean removeExisting = true;
    @Expose
    private boolean denySpawn = true;

    private String worldName;

    public void setWhitelist(@Nonnull EntityType[] whitelist) {
        this.whitelist = whitelist;
    }

    public void setBlacklist(@Nonnull EntityType[] blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public void start() {
        worldName = getPhase().getFeature(MapFeature.class).getWorld().getName();

        if (removeExisting) {
            for (Entity entity : getPhase().getFeature(MapFeature.class).getWorld().getEntities()) {
                if (blacklist.length != 0) {
                    if (Arrays.stream(blacklist).anyMatch(m -> m.equals(entity.getType()))) {
                        entity.remove();
                    }
                } else if (whitelist.length != 0) {
                    if (Arrays.stream(whitelist).noneMatch(m -> m.equals(entity.getType()))) {
                        entity.remove();
                    }
                } else {
                    entity.remove();
                }
            }
        }
    }

    @Nonnull
    @Override
    public Class[] getDependencies() {
        return new Class[]{MapFeature.class};
    }

    @EventHandler
    public void onSpawn(@Nonnull CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            return;
        }
        if (!denySpawn) {
            return;
        }
        if (!event.getLocation().getWorld().getName().equals(worldName)) {
            return;
        }

        if (blacklist.length != 0) {
            if (Arrays.stream(blacklist).anyMatch(m -> m.equals(event.getEntityType()))) {
                event.setCancelled(true);
            }
        } else if (whitelist.length != 0) {
            if (Arrays.stream(whitelist).noneMatch(m -> m.equals(event.getEntityType()))) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    public void setRemoveExisting(boolean removeExisting) {
        this.removeExisting = removeExisting;
    }

    public void setDenySpawn(boolean denySpawn) {
        this.denySpawn = denySpawn;
    }
}
