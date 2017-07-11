package me.minidigger.voxelgameslib.feature.features;

import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.feature.FeatureInfo;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler
    public void onVoidDamage(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER)
            return;

        if(event.getCause().equals(EntityDamageEvent.DamageCause.VOID) && getPhase().getGame().isPlaying(event.getEntity().getUniqueId())) {
            Player player = (Player) event.getEntity();

            player.teleport(getPhase().getFeature(SpawnFeature.class).getSpawn(player.getUniqueId()));

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFellOutOfWorld(PlayerMoveEvent event) {
        // just in case damage is disabled
        if(event.getTo().getY() < 0 && getPhase().getGame().isPlaying(event.getPlayer().getUniqueId())) {
            event.getPlayer().teleport(getPhase().getFeature(SpawnFeature.class).getSpawn(event.getPlayer().getUniqueId()));
        }
    }
}
