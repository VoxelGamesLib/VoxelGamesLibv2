package me.minidigger.voxelgameslib.feature.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.team.Team;
import me.minidigger.voxelgameslib.user.User;

public class TeamFeature extends AbstractFeature {

    private List<Team> teams = new ArrayList<>();

    @Override
    public void start() {
        // TODO team feature
        for (User user : getPhase().getGame().getPlayers()) {
            Team team = new Team();
            team.put(user, user.getRating(getPhase().getGame().getGameMode()));
            teams.add(team);
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

    public Team[] getTeamsOrdered() {
        return new Team[0];
    }

    /**
     * @param user the user to check for
     * @return the team of the user, if present
     */
    public Optional<Team> getTeam(User user) {
        for (Team team : teams) {
            if (team.containsKey(user)) {
                return Optional.of(team);
            }
        }

        return Optional.empty();
    }
}
