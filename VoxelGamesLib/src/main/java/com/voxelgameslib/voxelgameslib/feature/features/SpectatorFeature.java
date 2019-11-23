package com.voxelgameslib.voxelgameslib.feature.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

import org.bukkit.GameMode;
import org.bukkit.Location;

@FeatureInfo(name = "SpectatorFeature", author = "MiniDigger", version = "1.0", description = "Handles spectating")
public class SpectatorFeature extends AbstractFeature {

    private static final Logger log = Logger.getLogger(SpectatorFeature.class.getName());

    private MarkerDefinition spawnMarker = new BasicMarkerDefinition("spec");
    private List<Vector3D> spawns = new ArrayList<>();
    private Map map;

    @GameEvent(filterPlayers = false, filterSpectators = true)
    public void onJoin(GameJoinEvent event) {
        event.getUser().getPlayer().setGameMode(GameMode.SPECTATOR);

        // spawn
        Optional<SpawnFeature> spawnFeature = getPhase().getOptionalFeature(SpawnFeature.class);
        if (spawns.size() > 0) {
            Location location = spawns.get(ThreadLocalRandom.current().nextInt(spawns.size()))
                    .toLocation(map.getLoadedName(getPhase().getGame().getUuid())).add(0.5, 0, 0.5);
            event.getUser().getPlayer().teleportAsync(location);
        } else if (spawnFeature.isPresent()) {
            event.getUser().getPlayer().teleportAsync(spawnFeature.get().getSpawn(event.getUser().getUuid()));
        } else if (event.getGame().getPlayers().size() > 0) {
            event.getUser().getPlayer().teleportAsync(event.getGame().getPlayers().get(0).getPlayer().getLocation());
        } else {
            log.warning("Could not figure out a spectator spawn point");
        }
    }

    @Override
    public void enable() {
        map = getPhase().getFeature(MapFeature.class).getMap();
        for (Marker marker : map.getMarkers(spawnMarker)) {
            spawns.add(marker.getLoc());
        }
    }

    @Nonnull
    @Override
    public MarkerDefinition[] getMarkers() {
        return new MarkerDefinition[]{spawnMarker};
    }

    @Nonnull
    @Override
    public List<Class<? extends Feature>> getSoftDependencies() {
        return Arrays.asList(SpawnFeature.class, MapFeature.class);
    }
}
