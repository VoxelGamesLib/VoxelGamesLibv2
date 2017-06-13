package me.minidigger.voxelgameslib.feature.features;

import javax.inject.Inject;

import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.feature.FeatureInfo;
import me.minidigger.voxelgameslib.scoreboard.Scoreboard;
import me.minidigger.voxelgameslib.scoreboard.ScoreboardHandler;

import org.bukkit.event.EventHandler;

@FeatureInfo(name = "ScoreboardFeature", author = "MiniDigger", version = "1.0",
        description = "Handles the scoreboard for all other features")
public class ScoreboardFeature extends AbstractFeature {

    @Inject
    private ScoreboardHandler scoreboardHandler;

    private Scoreboard scoreboard;

    @Override
    public void start() {
        getPhase().getGame().getPlayers().forEach(scoreboard::addUser);
        getPhase().getGame().getSpectators().forEach(scoreboard::addUser);
    }

    @Override
    public void stop() {
        scoreboard.removeAllLines();
        scoreboard.removeAllUsers();
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        scoreboard = scoreboardHandler.createScoreboard(getPhase().getGame().getGameMode().getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Feature>[] getDependencies() {
        return new Class[0];
    }

    @SuppressWarnings("JavaDoc")
    @EventHandler
    public void onJoin(GameJoinEvent event) {
        if (event.getGame().getUuid().equals(getPhase().getGame().getUuid())) {
            scoreboard.addUser(event.getUser());
        }
    }

    /**
     * @return the scoreboard instance that will be used for this phase
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
