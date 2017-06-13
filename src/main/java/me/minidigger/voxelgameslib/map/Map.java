package me.minidigger.voxelgameslib.map;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.user.User;

/**
 * A map. A map is a world that is playable in gamemodes. has all kind of extra informations about a
 * world.
 */
public class Map {

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

    private boolean loaded;

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
     */
    public void printSummery(@Nonnull User sender) {
        Lang.msg(sender, LangKey.WORLD_CREATOR_MAP_SUMMARY, info.getName(), worldName, info.getAuthor(),
                center, radius, info.getGamemodes());
        //TODO print summery of map
    }

    /**
     * @return the info of this map
     */
    public MapInfo getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(MapInfo info) {
        this.info = info;
    }

    /**
     * @return the center of this map
     */
    public Vector3D getCenter() {
        return center;
    }

    /**
     * @param center the center to set
     */
    public void setCenter(Vector3D center) {
        this.center = center;
    }

    /**
     * @return the radius of this map
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * @return the worldname of this map
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * @param worldName the worldname to set
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * @return the list of markers for this map
     */
    public List<Marker> getMarkers() {
        return markers;
    }

    /**
     * @param markers the list of markers to set
     */
    public void setMarkers(List<Marker> markers) {
        this.markers = markers;

    }

    /**
     * @return the list of chest markers for this map
     */
    public List<ChestMarker> getChestMarkers() {
        return chestMarkers;
    }

    /**
     * @param chestMarkers the chestmarkers to set
     */
    public void setChestMarkers(List<ChestMarker> chestMarkers) {
        this.chestMarkers = chestMarkers;
    }

    /**
     * @return if this map is loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * @param loaded if this map is loaded
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
