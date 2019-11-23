package com.voxelgameslib.voxelgameslib.components.elo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.Game;
import com.voxelgameslib.voxelgameslib.api.feature.features.DuelFeature;
import com.voxelgameslib.voxelgameslib.api.feature.features.TeamFeature;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;
import com.voxelgameslib.voxelgameslib.components.user.User;

import jskills.IPlayer;
import jskills.ITeam;
import jskills.Rating;
import jskills.SkillCalculator;
import jskills.trueskill.FactorGraphTrueSkillCalculator;

/**
 * Handles the calculation of "skill" for players
 */
public class EloHandler implements Handler {

    private static final Logger log = Logger.getLogger(EloHandler.class.getName());
    private SkillCalculator calculator = new FactorGraphTrueSkillCalculator();

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    /**
     * Handles the elo calculation for team based games
     *
     * @param game        the game that ended
     * @param teamFeature the team feature that managed the teams for this game
     */
    public void handleGameEnd(@Nonnull Game game, @Nonnull TeamFeature teamFeature) {
        List<ITeam> teams = new ArrayList<>();
        teams.addAll(teamFeature.getJSkillTeamsOrdered());
        Map<IPlayer, Rating> newRatings = calculator
                .calculateNewRatings(game.getGameMode(), teams,
                        IntStream.of(teamFeature.getJSkillTeamsOrdered().size()).toArray());
        update(game, newRatings);
    }

    /**
     * Handles the elo calculation for duel based games
     *
     * @param game        the game that ended
     * @param duelFeature the duel feature that managed the game
     * @param winner      the user that won
     */
    public void handleGameEnd(@Nonnull Game game, @Nonnull DuelFeature duelFeature, @Nonnull User winner) {
        List<ITeam> teams = new ArrayList<>();
        teams.add(new jskills.Team(duelFeature.getOne(),
                duelFeature.getOne().getRating(game.getGameMode())));
        teams.add(new jskills.Team(duelFeature.getTwo(),
                duelFeature.getTwo().getRating(game.getGameMode())));
        if (!winner.equals(duelFeature.getOne())) {
            Collections.reverse(teams);
        }
        Map<IPlayer, Rating> newRatings = calculator
                .calculateNewRatings(game.getGameMode(), teams, 1, 2);
        update(game, newRatings);
    }

    /**
     * Handles the elo calculation for generic games
     *
     * @param game  the game that ended
     * @param users the users which played this game, in order, first = winner
     */
    public void handleGameEnd(@Nonnull Game game, @Nonnull User... users) {
        List<ITeam> teams = new ArrayList<>();
        for (User user : users) {
            teams.add(new jskills.Team(user, user.getRating(game.getGameMode())));
        }
        Map<IPlayer, Rating> newRatings = calculator
                .calculateNewRatings(game.getGameMode(), teams,
                        IntStream.of(users.length).toArray());
        update(game, newRatings);
    }

    private void update(@Nonnull Game game, @Nonnull Map<IPlayer, Rating> newRatings) {
        for (IPlayer iPlayer : newRatings.keySet()) {
            if (!(iPlayer instanceof User)) {
                log.warning("WTF");
                continue;
            }

            User user = (User) iPlayer;
            user.saveRating(game.getGameMode(), newRatings.get(iPlayer));
            //TODO fixme
            log.info("New Rating for " + user.getRawDisplayName() + " is "
                    + newRatings.get(iPlayer).getMean() + "(" + newRatings.get(iPlayer).getStandardDeviation()
                    + ")");
        }
    }
}
