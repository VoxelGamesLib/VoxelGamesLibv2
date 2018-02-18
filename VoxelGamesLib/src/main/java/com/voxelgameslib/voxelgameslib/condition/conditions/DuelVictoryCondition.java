package com.voxelgameslib.voxelgameslib.condition.conditions;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.condition.AbstractVictoryCondition;
import com.voxelgameslib.voxelgameslib.condition.VictoryConditionInfo;
import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.features.DuelFeature;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@VictoryConditionInfo(name = "DuelVictoryCondition", author = "MiniDigger", version = "1.0", description = "Determines the winner for duel games")
public class DuelVictoryCondition extends AbstractVictoryCondition implements Listener {

    @Override
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.singletonList(DuelFeature.class);
    }

    @GameEvent
    public void onDeath(@Nonnull PlayerDeathEvent e) {
        if (completed()) return;
        DuelFeature duelFeature = getPhase().getFeature(DuelFeature.class);
        winner = duelFeature.getOther(e.getEntity().getUniqueId());
    }
}
