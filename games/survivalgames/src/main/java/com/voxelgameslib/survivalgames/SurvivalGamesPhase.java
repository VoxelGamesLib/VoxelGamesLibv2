package com.voxelgameslib.survivalgames;

import com.voxelgameslib.voxelgameslib.GameConstants;
import com.voxelgameslib.voxelgameslib.api.feature.features.AutoRespawnFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.GameModeFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.MapFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.MobFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.NoBlockBreakFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.SpawnFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.SpectatorFeature;
import com.voxelgameslib.voxelgameslib.api.phase.TimedPhase;

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
        noBlockBreakFeature.setWhitelist(new Material[]{Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
                Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.SUNFLOWER, Material.ROSE_BUSH,
                Material.ROSE_RED, Material.DANDELION_YELLOW, Material.DANDELION, Material.VINE, Material.BROWN_MUSHROOM,
                Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK, Material.RED_MUSHROOM, Material.MELON, Material.MELON_STEM,
                Material.PUMPKIN_STEM, Material.WHEAT, Material.PUMPKIN});
        addFeature(noBlockBreakFeature);
    }
}
