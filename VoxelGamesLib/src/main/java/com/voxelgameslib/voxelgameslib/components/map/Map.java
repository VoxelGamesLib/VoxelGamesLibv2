package com.voxelgameslib.voxelgameslib.components.map;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.internal.math.Vector3D;
import com.voxelgameslib.voxelgameslib.internal.lang.Lang;
import com.voxelgameslib.voxelgameslib.internal.lang.LangKey;
import com.voxelgameslib.voxelgameslib.components.user.User;

/**
 * A map. A map is a world that is playable in gamemodes. has all kind of extra informations about a world.
 */
public class Map {

    private static final Logger log = Logger.getLogger(Map.class.getName());
    @Expose
    private MapInfo info;
    @Expose
    private Vector3D center;
    @Expose
    private int radius;
    @Expose
    private String worldName;
    @Expose
    private List<Marker> markers = new ArrayList<>();
    @Expose
    private List<ChestMarker> chestMarkers = new ArrayList<>();

    private HashMap<UUID, String> loadedNames = new HashMap<>();

    /**
     * @param mapInfo   the map info for this map
     * @param worldName the name of the world
     * @param center    the center of this map
     * @param radius    the radius of this map
     */
    public Map(@Nonnull MapInfo mapInfo, @Nonnull String worldName, @Nonnull Vector3D center,
               int radius) {
        this.info = mapInfo;
        this.worldName = worldName;
        this.center = center;
        this.radius = radius;
        loadedNames = new HashMap<>();
    }

    /**
     * Tries to parse all marker data into the definitions
     *
     * @param mapHandler the maphandler that provides the marker definitions
     */
    public void initMarkers(@Nonnull MapHandler mapHandler) {
        markers.forEach(marker -> marker.setMarkerDefinition(mapHandler.createMarkerDefinition(marker.getData())));
    }

    /**
     * Gets a chestmarker that have the name as data
     *
     * @param name the name to search for
     * @return the optically found chestmarker
     */
    @Nonnull
    public Optional<ChestMarker> getChestMarker(@Nonnull String name) {
        return chestMarkers.stream().filter(chestMarker -> chestMarker.getData().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Prints a nice summary of this map
     *
     * @param sender the user to send the summary to
     */
    public void printSummary(@Nonnull User sender) {
        Lang.msg(sender, LangKey.WORLD_CREATOR_MAP_SUMMARY, info.getDisplayName(), worldName, info.getAuthor(),
                center, radius, info.getGamemodes());
        //TODO print summery of map
    }

    @Nonnull
    public String getLoadedName(@Nonnull UUID gameid) {
        if (loadedNames == null) {
            loadedNames = new HashMap<>();
        }
        return Optional.ofNullable(loadedNames.get(gameid))
                .orElseThrow(() -> new VoxelGameLibException("Map " + worldName + " was never loaded for game " + gameid));
    }

    public void load(@Nonnull UUID gameid, @Nonnull String name) {
        if (loadedNames == null) {
            loadedNames = new HashMap<>();
        }
        if (loadedNames.put(gameid, name) != null) {
            log.warning("Loaded the same map for the same game instance twice?");
        }
    }

    public boolean isLoaded(@Nonnull UUID gameid) {
        if (loadedNames == null) {
            loadedNames = new HashMap<>();
        }
        return loadedNames.containsKey(gameid);
    }

    public void unload(@Nonnull UUID gameid) {
        if (loadedNames == null) {
            loadedNames = new HashMap<>();
        }
        loadedNames.remove(gameid);
    }

    @Nonnull
    public List<Marker> getMarkers(@Nonnull MarkerDefinition spawnMarker) {
        return markers.stream().filter(marker -> spawnMarker.isOfSameType(marker.getMarkerDefinition())).collect(Collectors.toList());
    }

    public MapInfo getInfo() {
        return this.info;
    }

    public Vector3D getCenter() {
        return this.center;
    }

    public int getRadius() {
        return this.radius;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public List<Marker> getMarkers() {
        return this.markers;
    }

    public List<ChestMarker> getChestMarkers() {
        return this.chestMarkers;
    }

    public HashMap<UUID, String> getLoadedNames() {
        return this.loadedNames;
    }

    public void setInfo(MapInfo info) {
        this.info = info;
    }

    public void setCenter(Vector3D center) {
        this.center = center;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    public void setChestMarkers(List<ChestMarker> chestMarkers) {
        this.chestMarkers = chestMarkers;
    }

    public void setLoadedNames(HashMap<UUID, String> loadedNames) {
        this.loadedNames = loadedNames;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        final Map other = (Map) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$info = this.getInfo();
        final Object other$info = other.getInfo();
        if (this$info == null ? other$info != null : !this$info.equals(other$info)) return false;
        final Object this$center = this.getCenter();
        final Object other$center = other.getCenter();
        if (this$center == null ? other$center != null : !this$center.equals(other$center)) return false;
        if (this.getRadius() != other.getRadius()) return false;
        final Object this$worldName = this.getWorldName();
        final Object other$worldName = other.getWorldName();
        if (this$worldName == null ? other$worldName != null : !this$worldName.equals(other$worldName)) return false;
        final Object this$markers = this.getMarkers();
        final Object other$markers = other.getMarkers();
        if (this$markers == null ? other$markers != null : !this$markers.equals(other$markers)) return false;
        final Object this$chestMarkers = this.getChestMarkers();
        final Object other$chestMarkers = other.getChestMarkers();
        if (this$chestMarkers == null ? other$chestMarkers != null : !this$chestMarkers.equals(other$chestMarkers))
            return false;
        final Object this$loadedNames = this.getLoadedNames();
        final Object other$loadedNames = other.getLoadedNames();
        if (this$loadedNames == null ? other$loadedNames != null : !this$loadedNames.equals(other$loadedNames))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $info = this.getInfo();
        result = result * PRIME + ($info == null ? 43 : $info.hashCode());
        final Object $center = this.getCenter();
        result = result * PRIME + ($center == null ? 43 : $center.hashCode());
        result = result * PRIME + this.getRadius();
        final Object $worldName = this.getWorldName();
        result = result * PRIME + ($worldName == null ? 43 : $worldName.hashCode());
        final Object $markers = this.getMarkers();
        result = result * PRIME + ($markers == null ? 43 : $markers.hashCode());
        final Object $chestMarkers = this.getChestMarkers();
        result = result * PRIME + ($chestMarkers == null ? 43 : $chestMarkers.hashCode());
        final Object $loadedNames = this.getLoadedNames();
        result = result * PRIME + ($loadedNames == null ? 43 : $loadedNames.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Map;
    }

    public String toString() {
        return "Map(info=" + this.getInfo() + ", center=" + this.getCenter() + ", radius=" + this.getRadius() + ", worldName=" + this.getWorldName() + ", markers=" + this.getMarkers() + ", chestMarkers=" + this.getChestMarkers() + ", loadedNames=" + this.getLoadedNames() + ")";
    }
}
