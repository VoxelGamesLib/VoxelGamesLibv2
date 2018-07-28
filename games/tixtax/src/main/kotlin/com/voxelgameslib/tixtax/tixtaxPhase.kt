package com.voxelgameslib.tixtax

import com.voxelgameslib.kvgl.*
import com.voxelgameslib.voxelgameslib.GameConstants
import com.voxelgameslib.voxelgameslib.feature.features.GameModeFeature
import com.voxelgameslib.voxelgameslib.feature.features.MapFeature
import com.voxelgameslib.voxelgameslib.feature.features.SpawnFeature
import com.voxelgameslib.voxelgameslib.feature.features.TeamFeature
import com.voxelgameslib.voxelgameslib.phase.TimedPhase

class tixtaxPhase : TimedPhase() {

    override fun init() {
        name = "tixtaxPhase"
        ticks = 2 * 60 * GameConstants.TPS
        super.init()

        allowJoin = false
        allowSpectate = true

        addFeature<MapFeature> {
            shouldUnload = true
        }

        addFeature<SpawnFeature> {
            isRespawn = false
        }

        addFeature<GameModeFeature>()
        addFeature<tixtaxFeature>()
        addFeature<TeamFeature>()
    }
}
