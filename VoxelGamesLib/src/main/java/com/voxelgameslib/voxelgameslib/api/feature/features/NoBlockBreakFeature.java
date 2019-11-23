package com.voxelgameslib.voxelgameslib.api.feature.features;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

@FeatureInfo(name = "NoBlockBreakFeature", author = "MiniDigger", version = "1.0",
        description = "Small feature that blocks block breaking if active")
public class NoBlockBreakFeature extends AbstractFeature {

    @Expose
    private Material[] whitelist = new Material[0];
    @Expose
    private Material[] blacklist = new Material[0];

    /**
     * Sets the list with whitelisted materials. Enabling the whitelist means that ppl are allowed to break only
     * materials which are on the whitelist. to disabled the whitelist, pass an empty array.
     *
     * @param whitelist the new whitelist
     */
    public void setWhitelist(@Nonnull Material[] whitelist) {
        this.whitelist = whitelist;
    }

    /**
     * Sets the list with blacklisted materials. Enabling the blacklist means that ppl are allowed to break every
     * material other than those on the blacklist. to disabled the blacklist, pass an empty array
     *
     * @param blacklist the new blacklist
     */
    public void setBlacklist(@Nonnull Material[] blacklist) {
        this.blacklist = blacklist;
    }

    @SuppressWarnings({"JavaDoc", "Duplicates"})
    @GameEvent
    public void onBlockBreak(@Nonnull BlockBreakEvent event) {
        if (blacklist.length != 0) {
            if (Arrays.stream(blacklist).anyMatch(m -> m.equals(event.getBlock().getType()))) {
                event.setCancelled(true);
            }
        } else if (whitelist.length != 0) {
            if (Arrays.stream(whitelist).noneMatch(m -> m.equals(event.getBlock().getType()))) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }
}
