package com.voxelgameslib.survivalgames;

import com.voxelgameslib.voxelgameslib.GameConstants;
import com.voxelgameslib.voxelgameslib.feature.features.AutoRespawnFeature;
import com.voxelgameslib.voxelgameslib.feature.features.DuelFeature;
import com.voxelgameslib.voxelgameslib.feature.features.GameModeFeature;
import com.voxelgameslib.voxelgameslib.feature.features.KitFeature;
import com.voxelgameslib.voxelgameslib.feature.features.MapFeature;
import com.voxelgameslib.voxelgameslib.feature.features.MobFeature;
import com.voxelgameslib.voxelgameslib.feature.features.NoBlockBreakFeature;
import com.voxelgameslib.voxelgameslib.feature.features.SpawnFeature;
import com.voxelgameslib.voxelgameslib.feature.features.SpectatorFeature;
import com.voxelgameslib.voxelgameslib.phase.TimedPhase;

import org.bukkit.GameMode;
import org.bukkit.Material;

public class SurvivalGamesPhase extends TimedPhase {

    @Override
    public void init() {
        setName("SurvivalGamesPhase");
        setTicks(2 * 60 * GameConstants.TPS);
        super.init();
        setAllowJoin(false);
        setAllowSpectate(true);

        MapFeature mapFeature = getGame().createFeature(MapFeature.class, this);
        mapFeature.setShouldUnload(true);
        addFeature(mapFeature);

        SpawnFeature spawnFeature = getGame().createFeature(SpawnFeature.class, this);
        spawnFeature.setRespawn(false);
        spawnFeature.setInitialSpawn(false);
        addFeature(spawnFeature);

        GameModeFeature gameModeFeature = getGame().createFeature(GameModeFeature.class, this);
        gameModeFeature.setGameMode(GameMode.SURVIVAL);
        addFeature(gameModeFeature);

        SurvivalGamesFeature survivalgamesFeature = getGame().createFeature(SurvivalGamesFeature.class, this);
        addFeature(survivalgamesFeature);

        AutoRespawnFeature autoRespawnFeature = getGame().createFeature(AutoRespawnFeature.class, this);
        addFeature(autoRespawnFeature);

        MobFeature mobFeature = getGame().createFeature(MobFeature.class, this);
        addFeature(mobFeature);

        SpectatorFeature spectatorFeature = getGame().createFeature(SpectatorFeature.class, this);
        addFeature(spectatorFeature);

        NoBlockBreakFeature noBlockBreakFeature = getGame().createFeature(NoBlockBreakFeature.class, this);
        noBlockBreakFeature.setWhitelist(new Material[]{Material.LEAVES, Material.LEAVES_2, Material.YELLOW_FLOWER,
                Material.RED_ROSE, Material.VINE, Material.BROWN_MUSHROOM, Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2,
                Material.RED_MUSHROOM, Material.MELON_BLOCK, Material.WHEAT, Material.PUMPKIN});
        addFeature(noBlockBreakFeature);
    }
}
