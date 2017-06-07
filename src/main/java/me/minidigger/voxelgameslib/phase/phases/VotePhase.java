package me.minidigger.voxelgameslib.phase.phases;

import me.minidigger.voxelgameslib.GameConstants;
import me.minidigger.voxelgameslib.feature.features.ClearInventoryFeature;
import me.minidigger.voxelgameslib.feature.features.GameModeFeature;
import me.minidigger.voxelgameslib.feature.features.HealFeature;
import me.minidigger.voxelgameslib.feature.features.MapFeature;
import me.minidigger.voxelgameslib.feature.features.NoBlockBreakFeature;
import me.minidigger.voxelgameslib.feature.features.NoBlockPlaceFeature;
import me.minidigger.voxelgameslib.feature.features.NoDamageFeature;
import me.minidigger.voxelgameslib.feature.features.SpawnFeature;
import me.minidigger.voxelgameslib.feature.features.VoteFeature;
import me.minidigger.voxelgameslib.phase.TimedPhase;
import org.bukkit.GameMode;

/**
 * Can be used to vote on the next gamemode or the next map.
 */
public class VotePhase extends TimedPhase {

  @Override
  public void init() {
    setName("VotePhase");
    super.init();
    setAllowJoin(true);
    setAllowSpectate(false);
    setTicks(60 * GameConstants.TPS);

    addFeature(getGame().createFeature(VoteFeature.class, this));

    MapFeature mapFeature = getGame().createFeature(MapFeature.class, this);
    mapFeature.setShouldUnload(true);
    mapFeature.setMapGameDataKey("lobbymap");
    addFeature(mapFeature);

    SpawnFeature spawnFeature = getGame().createFeature(SpawnFeature.class, this);
    spawnFeature.setInitialSpawn(false);
    addFeature(spawnFeature);

    NoBlockBreakFeature noBlockBreakFeature = getGame()
        .createFeature(NoBlockBreakFeature.class, this);
    addFeature(noBlockBreakFeature);

    NoBlockPlaceFeature noBlockPlaceFeature = getGame()
        .createFeature(NoBlockPlaceFeature.class, this);
    addFeature(noBlockPlaceFeature);

    ClearInventoryFeature clearInventoryFeature = getGame()
        .createFeature(ClearInventoryFeature.class, this);
    addFeature(clearInventoryFeature);

    NoDamageFeature noDamageFeature = getGame().createFeature(NoDamageFeature.class, this);
    addFeature(noDamageFeature);

    HealFeature healFeature = getGame().createFeature(HealFeature.class, this);
    addFeature(healFeature);

    GameModeFeature gameModeFeature = getGame().createFeature(GameModeFeature.class, this);
    gameModeFeature.setGameMode(GameMode.SURVIVAL);
    addFeature(gameModeFeature);
  }
}
