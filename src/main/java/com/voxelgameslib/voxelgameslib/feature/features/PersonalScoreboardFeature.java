package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.common.collect.Iterables;
import com.voxelgameslib.voxelgameslib.components.scoreboard.*;
import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameLeaveEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.user.User;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

@FeatureInfo(name = "PersonalScoreboard", author = "aphel", version = "1.0",
        description = "Each player has their own individual scoreboard")
public class PersonalScoreboardFeature extends AbstractFeature {

    @Inject
    private ScoreboardHandler scoreboardHandler;

    @Getter
    private GlobalScoreboard globalScoreboard;
    @Getter
    private Map<User, Scoreboard> scoreboards = new HashMap<>();

    @Override
    public void start() {
        globalScoreboard = new GlobalScoreboard(scoreboards.values());

        getPhase().getGame().getAllUsers().forEach(user -> {
            Scoreboard scoreboard = scoreboardHandler.createScoreboard(getPhase().getGame().getGameMode().getName());
            scoreboard.addUser(user);
            scoreboards.put(user, scoreboard);
        });
    }

    @Override
    public void stop() {
        getPhase().getGame().getAllUsers().forEach(user -> {
            scoreboards.get(user).removeAllUsers();
            scoreboards.get(user).removeAllLines();
        });
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

    @GameEvent
    public void onJoin(GameJoinEvent event) {
        Scoreboard scoreboard = scoreboardHandler.createScoreboard(getPhase().getGame().getGameMode().getName());
        scoreboard.addUser(event.getUser());
        scoreboards.put(event.getUser(), scoreboard);
    }

    @GameEvent
    public void onQuit(GameLeaveEvent event) {
        Scoreboard scoreboard = scoreboards.get(event.getUser());
        scoreboard.removeAllLines();
        scoreboard.removeAllUsers();
        scoreboards.remove(event.getUser());
    }

    public Scoreboard getScoreboardForUser(User user) {
        return scoreboards.get(user);
    }

    /**
     * Allows for mass updating of personal scoreboards
     */
    public class GlobalScoreboard extends AbstractScoreboard {

        private Collection<Scoreboard> scoreboards;

        GlobalScoreboard(Collection<Scoreboard> scoreboards) {
            this.scoreboards = scoreboards;
        }

        @Override
        public void addLine(int key, @Nonnull ScoreboardLine line) {
            scoreboards.forEach(scoreboard -> scoreboard.addLine(key, line));
        }

        @Override
        public int addLine(@Nonnull ScoreboardLine line) {
            scoreboards.forEach(scoreboard -> scoreboard.addLine(line));

            return 0; // get the line again if you want to do something with it
        }

        @Override
        public void removeLine(@Nonnull String key) {
            scoreboards.forEach(scoreboard -> scoreboard.removeLine(key));
        }

        @Override
        public void removeLine(int key) {
            scoreboards.forEach(scoreboard -> scoreboard.removeLine(key));
        }

        @Override
        public void removeAllLines() {
            scoreboards.forEach(Scoreboard::removeAllLines);
        }

        @Nonnull
        @Override
        public Optional<ScoreboardLine> getLine(int key) {
            // call the getLines method
            return Optional.empty();
        }

        public List<ScoreboardLine> getLines(int key) {
            List<ScoreboardLine> lines = new ArrayList<>();

            scoreboards.forEach(scoreboard -> scoreboard.getLine(key).ifPresent(lines::add));

            return lines;
        }

        @Nonnull
        @Override
        public Optional<ScoreboardLine> getLine(@Nonnull String key) {
            return Optional.empty();
        }

        @Nonnull
        public List<ScoreboardLine> getLines(@Nonnull String key) {
            List<ScoreboardLine> lines = new ArrayList<>();

            scoreboards.forEach(scoreboard -> scoreboard.getLine(key).ifPresent(lines::add));

            return lines;
        }

        @Override
        public void addUser(@Nonnull User user) {
            // dont call this
        }

        @Override
        public void removeUser(@Nonnull User user) {
            // dont call this
        }

        @Override
        public boolean isAdded(@Nonnull User user) {
            for (Scoreboard scoreboard : scoreboards) {
                if (scoreboard.isAdded(user)) {
                    return true;
                }
            }

            return false;
        }

        @Nonnull
        @Override
        public List<User> getUsers() {
            // dont call this
            return new ArrayList<>();
        }

        @Override
        public void removeAllUsers() {
            scoreboards.forEach(Scoreboard::removeAllUsers);
        }

        @Nonnull
        @Override
        public String getTitle() {
            return Iterables.get(scoreboards, 0).getTitle();
        }

        @Override
        public void setTitle(@Nonnull String title) {
            scoreboards.forEach(scoreboard -> scoreboard.setTitle(title));
        }

        @Override
        public void setImplObject(org.bukkit.scoreboard.Scoreboard object) {
            //
        }

        @Override
        public StringScoreboardLine createAndAddLine(String content) {
            scoreboards.forEach(scoreboard -> scoreboard.createAndAddLine(content));

            return null; // get the line again if you want to do something with it
        }

        @Override
        public StringScoreboardLine createAndAddLine(String key, String content) {
            scoreboards.forEach(scoreboard -> scoreboard.createAndAddLine(key, content));

            return null; // get the line again if you want to do something with it
        }

        @Override
        public StringScoreboardLine createAndAddLine(int pos, String content) {
            scoreboards.forEach(scoreboard -> scoreboard.createAndAddLine(pos, content));

            return null; // get the line again if you want to do something with it
        }
    }
}
