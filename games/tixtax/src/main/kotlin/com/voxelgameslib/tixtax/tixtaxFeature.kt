package com.voxelgameslib.tixtax

import com.voxelgameslib.kvgl.*
import com.voxelgameslib.voxelgameslib.event.GameEvent
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo
import com.voxelgameslib.voxelgameslib.feature.features.TeamFeature
import org.bukkit.event.entity.PlayerDeathEvent

@FeatureInfo(name = "tixtaxFeature", author = "MiniDigger", version = "1.0.0", description = "tixtaxFeature description")
class tixtaxFeature : AbstractFeature() {

    private val log by logByClass<tixtaxFeature>()


    override fun start() {

        val teamFeature = phase.getFeature<TeamFeature>()

        phase.game.players.forEach { user ->
            teamFeature.getTeam(user).ifPresent { log.finer("${user.rawDisplayName} is on team ${it.name}") }
        }

    }

    override fun getDependencies() = depend(TeamFeature::class)


    @GameEvent
    fun PlayerDeathEvent.onDeath() {
        // stuff here I guess
    }

}
