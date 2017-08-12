package com.voxelgameslib.voxelgameslib.editmode;

import com.voxelgameslib.voxelgameslib.feature.features.AutoRespawnFeature;
import com.voxelgameslib.voxelgameslib.feature.features.BossBarFeature;
import com.voxelgameslib.voxelgameslib.feature.features.ClearInventoryFeature;
import com.voxelgameslib.voxelgameslib.feature.features.DoubleJumpFeature;
import com.voxelgameslib.voxelgameslib.feature.features.GameModeFeature;
import com.voxelgameslib.voxelgameslib.feature.features.JumpPadFeature;
import com.voxelgameslib.voxelgameslib.feature.features.MapFeature;
import com.voxelgameslib.voxelgameslib.feature.features.MobFeature;
import com.voxelgameslib.voxelgameslib.feature.features.SpawnFeature;
import com.voxelgameslib.voxelgameslib.feature.features.TimeFeature;
import com.voxelgameslib.voxelgameslib.feature.features.VoidTeleportFeature;
import com.voxelgameslib.voxelgameslib.phase.AbstractPhase;

import org.bukkit.boss.BarColor;

public class EditModePhase extends AbstractPhase {
    @Override
    public void init() {
        setName("EditMode");
        super.init();
        setAllowJoin(true);
        setAllowSpectate(false);

        AutoRespawnFeature autoRespawnFeature = getGame().createFeature(AutoRespawnFeature.class, this);
        addFeature(autoRespawnFeature);

        BossBarFeature bossBarFeature = getGame().createFeature(BossBarFeature.class, this);
        bossBarFeature.setMessage("Edit Mode active");
        bossBarFeature.setColor(BarColor.RED);
        addFeature(bossBarFeature);

        ClearInventoryFeature clearInventoryFeature = getGame().createFeature(ClearInventoryFeature.class, this);
        addFeature(clearInventoryFeature);

        DoubleJumpFeature doubleJumpFeature = getGame().createFeature(DoubleJumpFeature.class, this);
        addFeature(doubleJumpFeature);

        GameModeFeature gameModeFeature = getGame().createFeature(GameModeFeature.class, this);
        gameModeFeature.setGameMode(org.bukkit.GameMode.CREATIVE);
        addFeature(gameModeFeature);

        JumpPadFeature jumpPadFeature = getGame().createFeature(JumpPadFeature.class, this);
        addFeature(jumpPadFeature);

        VoidTeleportFeature voidTeleportFeature = getGame().createFeature(VoidTeleportFeature.class, this);
        addFeature(voidTeleportFeature);

        SpawnFeature spawnFeature = getGame().createFeature(SpawnFeature.class, this);
        addFeature(spawnFeature);

        MapFeature mapFeature = getGame().createFeature(MapFeature.class, this);
        addFeature(mapFeature);

        MobFeature mobFeature = getGame().createFeature(MobFeature.class, this);
        mobFeature.setRemoveExisting(false);
        addFeature(mobFeature);

        TimeFeature timeFeature = getGame().createFeature(TimeFeature.class, this);
        addFeature(timeFeature);
    }
}
