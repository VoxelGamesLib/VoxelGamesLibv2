package com.voxelgameslib.voxelgameslib.api.feature.features;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.api.game.DefaultGameData;

@Singleton
public class TeamFeature extends TeamSelectFeature {

    private static final Logger log = Logger.getLogger(TeamFeature.class.getName());

    @Override
    public void enable() {
        getPhase().setAllowJoin(false);

        DefaultGameData gameData = getPhase().getGame().getGameData(DefaultGameData.class).orElse(new DefaultGameData());
        teams = gameData.teams;
        if (teams == null || teams.size() == 0) {
            log.severe("You need to run team select before running team feature!");
            getPhase().getGame().abortGame();
        }
    }

    @Nonnull
    public List<jskills.Team> getJSkillTeamsOrdered() {
        return new ArrayList<>(); //TODO
    }
}
