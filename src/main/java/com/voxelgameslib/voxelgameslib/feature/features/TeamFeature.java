package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.game.DefaultGameData;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;

import lombok.extern.java.Log;

@Log
@Singleton
public class TeamFeature extends TeamSelectFeature {

    @Override
    public void start() {
        getPhase().setAllowJoin(false);

        DefaultGameData gameData = getPhase().getGame().getGameData(DefaultGameData.class).orElse(new DefaultGameData());
        teams = gameData.teams;
        if (teams == null || teams.size() == 0) {
            log.severe("You need to run team select before running team feature!");
            getPhase().getGame().abortGame();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {

    }

    @Override
    public Class[] getDependencies() {
        return new Class[0];
    }

    public List<jskills.Team> getJSkillTeamsOrdered() {
        return new ArrayList<>(); //TODO
    }
}
