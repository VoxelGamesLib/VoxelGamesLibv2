package me.minidigger.voxelgameslib.elo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import jskills.IPlayer;
import jskills.ITeam;
import jskills.Rating;
import jskills.SkillCalculator;
import jskills.trueskill.FactorGraphTrueSkillCalculator;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.feature.features.DuelFeature;
import me.minidigger.voxelgameslib.feature.features.TeamFeature;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.user.User;

/**
 * Created by Martin on 28.01.2017.
 */
@Log
public class EloHandler implements Handler {

  private SkillCalculator calculator = new FactorGraphTrueSkillCalculator();

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  /**
   * Handles the elo calculation for team based games
   *
   * @param game the game that ended
   * @param teamFeature the team feature that managed the teams for this game
   */
  public void handleGameEnd(Game game, TeamFeature teamFeature) {
    List<ITeam> teams = new ArrayList<>();
    teams.addAll(Arrays.asList(teamFeature.getTeamsOrdered()));
    Map<IPlayer, Rating> newRatings = calculator
        .calculateNewRatings(game.getGameMode().getRatingInfo(), teams,
            IntStream.of(teamFeature.getTeamsOrdered().length).toArray());
    update(game, newRatings);
  }

  /**
   * Handles the elo calculation for duel based games
   *
   * @param game the game that ended
   * @param duelFeature the duel feature that managed the game
   * @param winner the user that won
   */
  public void handleGameEnd(Game game, DuelFeature duelFeature, User winner) {
    List<ITeam> teams = new ArrayList<>();
    teams.add(new jskills.Team(duelFeature.getOne(),
        duelFeature.getOne().getData().getRating(game.getGameMode())));
    teams.add(new jskills.Team(duelFeature.getTwo(),
        duelFeature.getTwo().getData().getRating(game.getGameMode())));
    if (!winner.equals(duelFeature.getOne())) {
      Collections.reverse(teams);
    }
    Map<IPlayer, Rating> newRatings = calculator
        .calculateNewRatings(game.getGameMode().getRatingInfo(), teams, 1, 2);
    update(game, newRatings);
  }

  /**
   * Handles the elo calculation for generic games
   *
   * @param game the game that ended
   * @param users the users which played this game, in order, first = winner
   */
  public void handleGameEnd(Game game, User... users) {
    List<ITeam> teams = new ArrayList<>();
    for (User user : users) {
      teams.add(new jskills.Team(user, user.getData().getRating(game.getGameMode())));
    }
    Map<IPlayer, Rating> newRatings = calculator
        .calculateNewRatings(game.getGameMode().getRatingInfo(), teams,
            IntStream.of(users.length).toArray());
    update(game, newRatings);
  }

  private void update(Game game, Map<IPlayer, Rating> newRatings) {
    for (IPlayer iPlayer : newRatings.keySet()) {
      if (!(iPlayer instanceof User)) {
        log.warning("WTF");
        continue;
      }

      User user = (User) iPlayer;
      user.getData().saveRating(game.getGameMode(), newRatings.get(iPlayer));
      log.info("New Rating for " + user.getData().getDisplayName() + " is "
          + newRatings.get(iPlayer).getMean() + "(" + newRatings.get(iPlayer).getStandardDeviation()
          + ")");
    }
  }
}
