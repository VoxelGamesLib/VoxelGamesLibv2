package com.voxelgameslib.voxelgameslib.phase.phases;

import com.voxelgameslib.voxelgameslib.feature.features.BossBarFeature;
import com.voxelgameslib.voxelgameslib.feature.features.ClearInventoryFeature;
import com.voxelgameslib.voxelgameslib.feature.features.GameModeFeature;
import com.voxelgameslib.voxelgameslib.feature.features.HealFeature;
import com.voxelgameslib.voxelgameslib.feature.features.LobbyFeature;
import com.voxelgameslib.voxelgameslib.feature.features.MapFeature;
import com.voxelgameslib.voxelgameslib.feature.features.MobFeature;
import com.voxelgameslib.voxelgameslib.feature.features.NoBlockBreakFeature;
import com.voxelgameslib.voxelgameslib.feature.features.NoBlockPlaceFeature;
import com.voxelgameslib.voxelgameslib.feature.features.NoDamageFeature;
import com.voxelgameslib.voxelgameslib.feature.features.NoHungerLossFeature;
import com.voxelgameslib.voxelgameslib.feature.features.ScoreboardFeature;
import com.voxelgameslib.voxelgameslib.feature.features.SpawnFeature;
import com.voxelgameslib.voxelgameslib.phase.AbstractPhase;

import org.bukkit.GameMode;

/**
 * The lobby phase is the phase that part before the game starts. players can do cool activities while they wait for
 * more players to join.
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
        mapFeature.setType(MapFeature.Type.LOBBY);
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
        gameModeFeature.setGameMode(GameMode.ADVENTURE);
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

        MobFeature mobFeature = getGame().createFeature(MobFeature.class, this);
        addFeature(mobFeature);
    }
}
