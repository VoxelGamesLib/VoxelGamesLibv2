package com.voxelgameslib.voxelgameslib.condition;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.voxelgameslib.voxelgameslib.components.team.Team;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.phase.Phase;
import com.voxelgameslib.voxelgameslib.phase.PhaseTypeAdapter;
import com.voxelgameslib.voxelgameslib.user.User;

public abstract class AbstractVictoryCondition implements VictoryCondition {

    @Expose
    private String classname;

    private Phase phase;
    protected User winner;
    protected Team winnerTeam;

    public AbstractVictoryCondition() {
        classname = getClass().getName().replace(VictoryConditionTypeAdapter.DEFAULT_PATH + ".", "");
    }

    @Override
    public Phase getPhase() {
        return phase;
    }

    @Nullable
    @Override
    public User getWinner() {
        return winner;
    }

    @Nullable
    @Override
    public Team getWinnerTeam() {
        return winnerTeam;
    }

    @Override
    public boolean completed() {
        return winner != null || winnerTeam != null;
    }

    @Override
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        if (getClass().isAnnotationPresent(VictoryConditionInfo.class)) {
            return getClass().getAnnotation(VictoryConditionInfo.class).name();
        }
        return getClass().getSimpleName();
    }

    @Override
    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
