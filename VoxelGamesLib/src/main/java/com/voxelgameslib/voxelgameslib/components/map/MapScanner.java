package com.voxelgameslib.voxelgameslib.components.map;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.components.placeholders.SkullPlaceHolders;
import com.voxelgameslib.voxelgameslib.api.exception.MapException;
import com.voxelgameslib.voxelgameslib.internal.math.Vector3D;
import com.voxelgameslib.voxelgameslib.util.utils.DirectionUtil;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

/**
 * Scans the map for markers.
 */
@Singleton
public class MapScanner {

    private static final Logger log = Logger.getLogger(MapScanner.class.getName());
    @Inject
    private MapHandler mapHandler;
    @Inject
    private SkullPlaceHolders skullPlaceHolders;

    /**
     * Scans a map for markers
     *
     * @param map    the map to scan
     * @param gameid the id of the game that the map belongs to
     */
    public void scan(@Nonnull Map map, @Nonnull UUID gameid) {
        searchForMarkers(map, map.getCenter(), map.getRadius(), gameid);

        List<Marker> errored = new ArrayList<>();

        map.getMarkers().stream().filter(marker -> marker.getData().startsWith("chest:"))
                .forEach(marker -> {
                    String name = marker.getData().replace("chest:", "");
                    if (!map.getChestMarker(name).isPresent()) {
                        log.warning(
                                "Could not find a chest " + name + " for marker at " + marker.getLoc().toString());
                        errored.add(marker);
                    }
                });

        map.getMarkers().removeAll(errored);

        List<ChestMarker> errored2 = new ArrayList<>();

        map.getChestMarkers().stream().filter(marker -> marker.getData().startsWith("container.chest"))
                .forEach(marker -> {
                    log.warning("Found unnamed chest at " + marker.getLoc().toString());
                    errored2.add(marker);
                });

        map.getChestMarkers().removeAll(errored2);
    }

    /**
     * Searches the map for "markers". Most of the time these are implemented as tile entities (skulls)
     *
     * @param map    the map to scan
     * @param center the center location
     * @param range  the range in where to scan
     */
    public void searchForMarkers(@Nonnull Map map, @Nonnull Vector3D center, int range, @Nonnull UUID gameid) {
        World world = Bukkit.getWorld(map.getLoadedName(gameid));
        if (world == null) {
            throw new MapException("Could not find world " + map.getLoadedName(gameid) + "(" + map.getInfo().getDisplayName() + ")" + ". Is it loaded?");
        }

        List<Marker> markers = new ArrayList<>();
        List<ChestMarker> chestMarkers = new ArrayList<>();

        int startX = (int) center.getX();
        int startY = (int) center.getZ();

        int minX = Math.min(startX - range, startX + range);
        int minZ = Math.min(startY - range, startY + range);

        int maxX = Math.max(startX - range, startX + range);
        int maxZ = Math.max(startY - range, startY + range);

        for (int x = minX; x <= maxX; x += 16) {
            for (int z = minZ; z <= maxZ; z += 16) {
                Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
                for (BlockState te : chunk.getTileEntities()) {
                    if (te.getType() == Material.PLAYER_HEAD) {
                        Skull skull = (Skull) te;
                        String markerData = getMarkerData(skull);
                        if (markerData == null) continue;
                        MarkerDefinition markerDefinition = mapHandler.createMarkerDefinition(markerData);
                        markers.add(new Marker(new Vector3D(skull.getX(), skull.getY(), skull.getZ()),
                                DirectionUtil.directionToYaw(skull.getRotation()),
                                markerData, markerDefinition));
                    } else if (te.getType() == Material.CHEST) {
                        Chest chest = (Chest) te;
                        String name = chest.getBlockInventory().getName();
                        ItemStack[] items = new ItemStack[chest.getBlockInventory()
                                .getStorageContents().length];
                        for (int i = 0; i < items.length; i++) {
                            ItemStack is = chest.getBlockInventory().getItem(i);
                            if (is == null) {
                                items[i] = new ItemStack(Material.AIR);
                            } else {
                                items[i] = is;
                            }
                        }
                        chestMarkers
                                .add(new ChestMarker(new Vector3D(chest.getX(), chest.getY(), chest.getZ()), name,
                                        items));
                    }
                }
            }
        }

        map.setMarkers(markers);
        map.setChestMarkers(chestMarkers);
    }

    private void handleSkull(@Nonnull Skull skull) {

    }

    @Nullable
    private String getMarkerData(@Nonnull Skull skull) {
        if (skull.getOwningPlayer() != null) {
            String markerData = skull.getOwningPlayer().getName();
            if (markerData == null) {
                //log.warning("owning player name null?!");
                markerData = skull.getOwner();
                if (markerData == null) {
                    log.warning("Could not find data about the owner for the skull at " + skull.getLocation().toVector());
                    markerData = "undefined";
                }
            }

            if (isPlaceholder(markerData)) {
                return null;
            } else {
                return markerData;
            }
        } else {
            return null;
        }
    }

    private boolean isPlaceholder(String key) {
        for (String k : skullPlaceHolders.getPlaceHolders().keySet()) {
            if (key.startsWith(k)) {
                return true;
            }
        }

        return false;
    }
}
