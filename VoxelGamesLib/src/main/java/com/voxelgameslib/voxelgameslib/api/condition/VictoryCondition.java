package com.voxelgameslib.voxelgameslib.api.condition;

import java.util.List;
import javax.annotation.Nullable;

import com.voxelgameslib.voxelgameslib.components.team.Team;
import com.voxelgameslib.voxelgameslib.api.feature.Feature;
import com.voxelgameslib.voxelgameslib.api.phase.Phase;
import com.voxelgameslib.voxelgameslib.components.user.User;

public interface VictoryCondition {

    /**
     * Checks if this condition has been met
     *
     * @return true if the condition has been met
     */
    boolean completed();

    List<Class<? extends Feature>> getDependencies();

    Phase getPhase();

    @Nullable
    User getWinner();

    @Nullable
    Team getWinnerTeam();

    String getName();

    void setPhase(Phase phase);
}
