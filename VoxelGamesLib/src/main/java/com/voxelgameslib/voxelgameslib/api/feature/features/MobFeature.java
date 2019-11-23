package com.voxelgameslib.voxelgameslib.api.feature.features;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.Feature;
import com.voxelgameslib.voxelgameslib.util.utils.EntityUtil;

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

    public MobFeature() {
        setWhitelist(EntityUtil.getAll(EntityUtil.Type.OTHER, EntityUtil.Type.UTILITY, EntityUtil.Type.PROJECTILE));
    }

    public void setWhitelist(@Nonnull EntityType... whitelist) {
        this.whitelist = whitelist;
    }

    public void addWhitelist(@Nonnull EntityType... whitelist) {
        ArrayUtils.addAll(this.whitelist, whitelist);
    }

    public void setBlacklist(@Nonnull EntityType... blacklist) {
        this.blacklist = blacklist;
    }

    public void addBlacklist(@Nonnull EntityType... blacklist) {
        ArrayUtils.addAll(this.blacklist, blacklist);
    }

    @Override
    public void enable() {
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
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.singletonList(MapFeature.class);
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
