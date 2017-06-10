package me.minidigger.voxelgameslib.phase.phases;

import me.minidigger.voxelgameslib.feature.features.BossBarFeature;
import me.minidigger.voxelgameslib.feature.features.ClearInventoryFeature;
import me.minidigger.voxelgameslib.feature.features.GameModeFeature;
import me.minidigger.voxelgameslib.feature.features.HealFeature;
import me.minidigger.voxelgameslib.feature.features.LobbyFeature;
import me.minidigger.voxelgameslib.feature.features.MapFeature;
import me.minidigger.voxelgameslib.feature.features.NoBlockBreakFeature;
import me.minidigger.voxelgameslib.feature.features.NoBlockPlaceFeature;
import me.minidigger.voxelgameslib.feature.features.NoDamageFeature;
import me.minidigger.voxelgameslib.feature.features.NoHungerLossFeature;
import me.minidigger.voxelgameslib.feature.features.ScoreboardFeature;
import me.minidigger.voxelgameslib.feature.features.SpawnFeature;
import me.minidigger.voxelgameslib.phase.AbstractPhase;
import org.bukkit.GameMode;

/**
 * The lobby phase is the phase that part before the game starts. players can do cool activities
 * while they wait for more players to join.
 */
public class LobbyPhase extends AbstractPhase {

  @Override
  public void init() {
    setName("LobbyPhase");
    super.init();
    setAllowJoin(true);
    setAllowSpectate(false);

    MapFeature mapFeature = getGame().createFeature(MapFeature.class, this);
    mapFeature.setShouldUnload(false);
    mapFeature.setMapGameDataKey("lobbymap");
    addFeature(mapFeature);

    SpawnFeature spawnFeature = getGame().createFeature(SpawnFeature.class, this);
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

    LobbyFeature lobbyFeature = getGame().createFeature(LobbyFeature.class, this);
    addFeature(lobbyFeature);

    NoHungerLossFeature noHungerLossFeature = getGame()
        .createFeature(NoHungerLossFeature.class, this);
    addFeature(noHungerLossFeature);

    ScoreboardFeature scoreboardFeature = getGame().createFeature(ScoreboardFeature.class, this);
    addFeature(scoreboardFeature);

    BossBarFeature bossBarFeature = getGame().createFeature(BossBarFeature.class, this);
    addFeature(bossBarFeature);
  }
}
