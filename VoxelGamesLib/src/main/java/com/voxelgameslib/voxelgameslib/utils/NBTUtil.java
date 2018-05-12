package com.voxelgameslib.voxelgameslib.utils;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;

/**
 * Util to nicely set nbt data
 */
public class NBTUtil {

    /**
     * Sets a player profile onto the given tag
     *
     * @param nbt           the tag to set the profile to
     * @param playerProfile the profile to set
     */
    public static void setPlayerProfile(NbtCompound nbt, PlayerProfile playerProfile) {
        nbt.put("Id", (playerProfile.getId() == null ? UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerProfile.getName()).getBytes()) : playerProfile.getId()).toString());
        nbt.put("Name", playerProfile.getName());
        if(!nbt.containsKey("Properties")) return;
        NbtCompound properties = nbt.getCompound("Properties");
        NbtList list = properties.getList("textures");
        Map<String, NbtBase> texture = (Map<String, NbtBase>) list.getValue(0);
        for (ProfileProperty property : playerProfile.getProperties()) {
            texture.put("name", NbtFactory.of("name", property.getValue()));
            texture.put("Signature", NbtFactory.of("Signature", property.getSignature()));
            texture.put("Value", NbtFactory.of("Value", property.getValue()));
        }
    }
}
