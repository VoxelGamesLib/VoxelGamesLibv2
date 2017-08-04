package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@FeatureInfo(name = "VoidTeleportFeature", author = "aphel", version = "1.0",
        description = "Teleports player to spawn if they fall into the void")
public class VoidTeleportFeature extends AbstractFeature {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {

    }

    @Override
    public Class[] getDependencies() {
        return new Class[]{SpawnFeature.class};
    }

    @GameEvent
    public void onVoidDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return;

        if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            Player player = (Player) event.getEntity();

            player.teleport(getPhase().getFeature(SpawnFeature.class).getSpawn(player.getUniqueId()));

            event.setCancelled(true);
        }
    }

    @GameEvent
    public void onFellOutOfWorld(PlayerMoveEvent event) {
        // just in case damage is disabled
        if (event.getTo().getY() < 0) {
            event.getPlayer().teleport(getPhase().getFeature(SpawnFeature.class).getSpawn(event.getPlayer().getUniqueId()));
        }
    }
}
