package com.voxelgameslib.survivalgames;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.feature.features.MapFeature;
import com.voxelgameslib.voxelgameslib.map.BasicMarkerDefinition;
import com.voxelgameslib.voxelgameslib.map.MarkerDefinition;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.PistonBaseMaterial;

@FeatureInfo(name = "PodFeature", author = "MiniDigger", version = "1.0", description = "Is responsible for pushing up players so they can leave their spawn pod")
public class PodFeature extends AbstractFeature {

    private MarkerDefinition spawnMarker = new BasicMarkerDefinition("spawn");
    private static final Logger log = Logger.getLogger(PodFeature.class.getName());

    @Inject
    private VoxelGamesLib voxelGamesLib;

    @Override
    public void enable() {
        MapFeature mapFeature = getPhase().getFeature(MapFeature.class);
        List<Location> locations = mapFeature.getMap().getMarkers(spawnMarker).stream()
                .map(marker -> marker.getLoc().toLocation(mapFeature.getWorld().getName())).collect(Collectors.toList());

        powerRelative(locations, 2);

        Bukkit.getScheduler().runTaskLater(voxelGamesLib, () -> powerRelative(locations, 0), 20);
    }

    private void powerRelative(List<Location> locations, int rel) {
        locations.forEach(loc -> {
            Block block = loc.getBlock().getRelative(BlockFace.DOWN, rel);
            if (block.getType() != Material.PISTON_STICKY_BASE) {
                log.warning("Invalid block at " + block.getLocation().toVector().toString() + ": Expected a PistonBase, got " + block.getType());
                return;
            }
            PistonBaseMaterial piston = (PistonBaseMaterial) block.getState().getData();
            piston.setPowered(true);
            block.getState().update(true);
        });
    }

    @Nonnull
    @Override
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.singletonList(MapFeature.class);
    }

    @Nonnull
    @Override
    public MarkerDefinition[] getMarkers() {
        return new MarkerDefinition[]{spawnMarker};
    }
}
