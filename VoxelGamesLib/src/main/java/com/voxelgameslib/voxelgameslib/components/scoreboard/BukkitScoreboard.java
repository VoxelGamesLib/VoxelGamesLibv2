package com.voxelgameslib.voxelgameslib.components.scoreboard;

import java.util.Optional;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.components.user.User;
import com.voxelgameslib.voxelgameslib.util.utils.RandomUtil;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BukkitScoreboard extends AbstractScoreboard {

    private Scoreboard scoreboard;
    private Objective objective;

    @Override
    public void setImplObject(@Nonnull Scoreboard object) {
        scoreboard = object;
        objective = scoreboard.registerNewObjective("VoxelGamesLib", "dummy"); // todo dont forget to remove dummy
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Nonnull
    @Override
    public String getTitle() {
        return objective.getDisplayName();
    }

    @Override
    public void setTitle(@Nonnull String title) {
        objective.setDisplayName(title);
    }

    @Override
    public void addLine(int key, @Nonnull ScoreboardLine line) {
        super.addLine(key, line);
    }

    @Override
    public void removeLine(int key) {
        Optional<ScoreboardLine> line = getLine(key);
        if (!line.isPresent()) {
            super.removeLine(key);
            return;
        }

        scoreboard.resetScores(line.get().getValue());

        super.removeLine(key);
    }

    @Override
    public void addUser(@Nonnull User user) {
        super.addUser(user);

        user.getPlayer().setScoreboard(scoreboard);
    }

    @Override
    public void removeUser(@Nonnull User user) {
        super.removeUser(user);

        user.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    @Nonnull
    public StringScoreboardLine createAndAddLine(@Nonnull String content) {
        Team team = scoreboard.registerNewTeam("line" + RandomUtil.generateString(8));
        BukkitStringScoreboardLine scoreboardLine = new BukkitStringScoreboardLine(content, team);
        int score = addLine(scoreboardLine);
        String entry = scoreboardLine.setScore(score);
        objective.getScore(entry).setScore(score);
        scoreboardLine.setValue(content);
        return scoreboardLine;
    }

    @Override
    @Nonnull
    public StringScoreboardLine createAndAddLine(int pos, @Nonnull String content) {
        Team team = scoreboard.registerNewTeam("line" + pos);
        BukkitStringScoreboardLine scoreboardLine = new BukkitStringScoreboardLine(content, team);
        addLine(pos, scoreboardLine);
        String entry = scoreboardLine.setScore(pos);
        objective.getScore(entry).setScore(pos);
        scoreboardLine.setValue(content);
        return scoreboardLine;
    }

    @Override
    @Nonnull
    public StringScoreboardLine createAndAddLine(@Nonnull String key, @Nonnull String content) {
        Team team = scoreboard.registerNewTeam("line" + key);
        BukkitStringScoreboardLine scoreboardLine = new BukkitStringScoreboardLine(content, team);
        int score = addLine(key, scoreboardLine);
        String entry = scoreboardLine.setScore(score);
        objective.getScore(entry).setScore(score);
        scoreboardLine.setValue(content);
        return scoreboardLine;
    }
}
