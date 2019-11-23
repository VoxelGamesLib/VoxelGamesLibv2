package com.voxelgameslib.voxelgameslib.api.feature.features;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.api.event.GameEvent;
import com.voxelgameslib.voxelgameslib.api.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.components.scoreboard.Scoreboard;
import com.voxelgameslib.voxelgameslib.components.scoreboard.ScoreboardHandler;

@FeatureInfo(name = "ScoreboardFeature", author = "MiniDigger", version = "1.0",
        description = "Handles the scoreboard for all other features")
public class ScoreboardFeature extends AbstractFeature {

    @Inject
    private ScoreboardHandler scoreboardHandler;

    private Scoreboard scoreboard;

    @Override
    public void enable() {
        getPhase().getGame().getPlayers().forEach(scoreboard::addUser);
        getPhase().getGame().getSpectators().forEach(scoreboard::addUser);
    }

    @Override
    public void disable() {
        scoreboard.removeAllLines();
        scoreboard.removeAllUsers();
    }

    @Override
    public void init() {
        scoreboard = scoreboardHandler.createScoreboard(getPhase().getGame().getGameMode().getName());
    }

    @SuppressWarnings("JavaDoc")
    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent event) {
        scoreboard.addUser(event.getUser());
    }

    /**
     * @return the scoreboard instance that will be used for this phase
     */
    @Nonnull
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
