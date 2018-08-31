package com.voxelgameslib.voxelgameslib.feature.features;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GamePreLeaveEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

@FeatureInfo(name = "DoubleJumpFeature", author = "MiniDigger", version = "1.0", description = "Lets player jump higher")
public class DoubleJumpFeature extends AbstractFeature {

    private List<UUID> disabled = new ArrayList<UUID>();

    public void setDisable(@Nonnull UUID id, boolean disable) {
        if (disable) {
            if (!disabled.contains(id)) {
                disabled.add(id);
            }
        } else {
            disabled.remove(id);
        }
    }


    @Override
    public void disable() {
        getPhase().getGame().getAllUsers().stream().map(User::getPlayer).forEach(player -> {
            player.setAllowFlight(false);
            player.setFlying(false);
        });
    }

    @GameEvent
    public void onQuit(@Nonnull GamePreLeaveEvent e) {
        e.getUser().getPlayer().setAllowFlight(false);
        e.getUser().getPlayer().setFlying(false);
    }

    @GameEvent
    public void e(@Nonnull PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (!disabled.contains(player.getUniqueId())) {
                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.setVelocity(player.getLocation().getDirection().multiply(1.6).setY(1));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 4, 1);
            }
        }
    }

    @GameEvent
    public void e(@Nonnull PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getBlock().getRelative(0, -1, 0).getType() != Material.AIR && (!player.isFlying())) {
            player.setAllowFlight(true);
        }
    }

    public boolean isDisabled(@Nonnull UUID uuid) {
        return disabled.contains(uuid);
    }
}
