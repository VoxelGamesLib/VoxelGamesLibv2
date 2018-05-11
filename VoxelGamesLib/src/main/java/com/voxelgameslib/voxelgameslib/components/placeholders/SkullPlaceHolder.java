package com.voxelgameslib.voxelgameslib.components.placeholders;

import com.destroystokyo.paper.profile.PlayerProfile;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a skull that can dynamically change its owner
 */
public interface SkullPlaceHolder {

    PlayerProfile apply(String name, Player player, Location location);
}
