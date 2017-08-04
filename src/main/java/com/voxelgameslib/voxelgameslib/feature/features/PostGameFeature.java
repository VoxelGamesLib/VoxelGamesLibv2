package com.voxelgameslib.voxelgameslib.feature.features;

import com.voxelgameslib.voxelgameslib.components.team.Team;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.phase.TimedPhase;
import com.voxelgameslib.voxelgameslib.user.User;

import javax.annotation.Nullable;

/**
 * This class implements core logic for a post-game feature to act like a summary for the game.
 * <p>
 * Generally, this should be extended by your game to incorporate any game-specific logic and do
 * tasks like add the proper values to the scoreboard. Call the super methods before you do
 * anything.
 */
@FeatureInfo(name = "PostGameFeature", author = "aphelion", version = "1.0",
        description = "Implements post-game logic")
public class PostGameFeature extends AbstractFeature {

    private PersonalScoreboardFeature.GlobalScoreboard globalScoreboard;

    private Team winningTeam = null;
    private User winningUser = null;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        initialiseScoreboard();
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {
        if (getPhase() instanceof TimedPhase) {
            if (((TimedPhase) getPhase()).getTicks() == 2) {
                if (winningTeam != null) {
                    getPhase().getGame().endGame(winningTeam, null);
                } else if (winningUser != null) {
                    getPhase().getGame().endGame(null, winningUser);
                } else {
                    getPhase().getGame().endGame(null, null);
                }
            }
        }
    }

    /**
     * Sets the winners of the game. Pass the winning team.
     *
     * @param winningTeam team that won the game
     */
    public void setWinner(@Nullable Team winningTeam) {
        setWinner(winningTeam, null);
    }

    /**
     * Sets the winners of the game. Pass the winning team.
     *
     * @param winningUser user that won the game
     */
    public void setWinner(@Nullable User winningUser) {
        setWinner(null, winningUser);
    }

    /**
     * Sets the winners of the game. Call this by its two methods.
     */
    private void setWinner(@Nullable Team winningTeam, @Nullable User winningUser) {
        if (winningTeam != null) {
            this.winningTeam = winningTeam;
        } else if (winningUser != null) {
            this.winningUser = winningUser;
        } else {
            // nobody won
        }
    }

    public void initialiseScoreboard() {
        globalScoreboard = getPhase().getFeature(PersonalScoreboardFeature.class).getGlobalScoreboard();
    }

    @Override
    public Class[] getDependencies() {
        return new Class[]{PersonalScoreboardFeature.class};
    }
}
