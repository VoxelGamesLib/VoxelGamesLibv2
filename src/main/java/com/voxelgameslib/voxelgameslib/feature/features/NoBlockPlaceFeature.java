package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;

@FeatureInfo(name = "NoBlockPlaceFeature", author = "MiniDigger", version = "1.0",
        description = "Small feature that blocks block placing if active")
public class NoBlockPlaceFeature extends AbstractFeature {

    @Expose
    private Material[] whitelist = new Material[0];
    @Expose
    private Material[] blacklist = new Material[0];

    /**
     * Sets the list with whitelisted materials. Enabling the whitelist means that ppl are allowed
     * to place only materials which are on the whitelist. to disabled the whitelist, pass an empty
     * array.
     *
     * @param whitelist the new whitelist
     */
    public void setWhitelist(Material[] whitelist) {
        this.whitelist = whitelist;
    }

    /**
     * Sets the list with blacklisted materials. Enabling the blacklist means that ppl are allowed
     * to place every material other than those on the blacklist. to disabled the blacklist, pass an
     * empty array
     *
     * @param blacklist the new blacklist
     */
    public void setBlacklist(Material[] blacklist) {
        this.blacklist = blacklist;
    }

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
        return new Class[0];
    }

    @SuppressWarnings({"JavaDoc", "Duplicates"})
    @GameEvent
    public void onBlockBreak(BlockPlaceEvent event) {
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
