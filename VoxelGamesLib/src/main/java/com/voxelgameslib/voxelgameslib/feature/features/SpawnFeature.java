package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.map.BasicMarkerDefinition;
import com.voxelgameslib.voxelgameslib.map.Map;
import com.voxelgameslib.voxelgameslib.map.Marker;
import com.voxelgameslib.voxelgameslib.map.MarkerDefinition;
import com.voxelgameslib.voxelgameslib.math.Vector3D;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerRespawnEvent;

@FeatureInfo(name = "SpawnFeature", author = "MiniDigger", version = "1.0",
        description = "Handles (re)spawning")
public class SpawnFeature extends AbstractFeature {

    private static final Logger log = Logger.getLogger(SpawnFeature.class.getName());

    @Expose
    private boolean isRespawn = true;
    @Expose
    private boolean isInitialSpawn = true;

    private List<Vector3D> spawns = new ArrayList<>();
    private Map map;

    private MarkerDefinition spawnMarker = new BasicMarkerDefinition("spawn");

    @Override
    public void enable() {
        map = getPhase().getFeature(MapFeature.class).getMap();
        for (Marker marker : map.getMarkers(spawnMarker)) {
            spawns.add(marker.getLoc());
        }

        if (spawns.size() == 0) {
            log.warning("Could not find any spawns on map " +
                    map.getInfo().getDisplayName() + "(" + map.getInfo().getWorldName() + ")");
            getPhase().getGame().abortGame();
            return;
        }

        if (isInitialSpawn) {
            for (User user : getPhase().getGame().getPlayers()) {
                user.getPlayer().teleport(getSpawn(user.getPlayer().getUniqueId()));
            }
        }
    }

    public void addSpawn(@Nonnull Vector3D spawn) {
        spawns.add(spawn);
    }

    /**
     * Generates a spawn location for that user
     *
     * @param id the uuid user which wants to spawn
     * @return the location he should spawn
     */
    @Nonnull
    public Location getSpawn(@Nonnull UUID id) {
        //TODO super fancy spawn algorithm
        return spawns.get(ThreadLocalRandom.current().nextInt(spawns.size()))
                .toLocation(map.getLoadedName(getPhase().getGame().getUuid())).add(0.5, 0, 0.5);
    }

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onRespawn(@Nonnull PlayerRespawnEvent e) {
        e.setRespawnLocation(getSpawn(e.getPlayer().getUniqueId()));
    }

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent e) {
        e.getUser().getPlayer().teleport(getSpawn(e.getUser().getUuid()));
    }

    @Override
    @Nonnull
    public MarkerDefinition[] getMarkers() {
        return new MarkerDefinition[]{spawnMarker};
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nonnull
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.singletonList(MapFeature.class);
    }

    /**
     * @param respawn if true, players will respawn after they died
     */
    public void setRespawn(boolean respawn) {
        isRespawn = respawn;
    }

    /**
     * @return if true, players will respawn after they died
     */
    public boolean isRespawn() {
        return isRespawn;
    }

    /**
     * @return if true, players will be spawned at the enable of this phase
     */
    public boolean isInitialSpawn() {
        return isInitialSpawn;
    }

    /**
     * @param initialSpawn if true, players will be spawned at the enable of this phase
     */
    public void setInitialSpawn(boolean initialSpawn) {
        isInitialSpawn = initialSpawn;
    }
}
