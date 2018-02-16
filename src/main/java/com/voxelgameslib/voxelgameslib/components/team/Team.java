package com.voxelgameslib.voxelgameslib.components.team;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.ChatColor;

import jskills.Rating;

public class Team {

    private jskills.Team jskillTeam;

    private int teamSize;
    private String name = "<unknown>";
    private List<User> players = new ArrayList<>();
    private ChatColor color;
    private Game game;

    public Team(int teamSize, @Nonnull String name, @Nonnull ChatColor color, @Nonnull Game game) {
        this.teamSize = teamSize;
        this.name = name;
        this.players = new ArrayList<>();
        this.color = color;
        this.game = game;

        this.jskillTeam = new jskills.Team();
    }

    public void join(@Nonnull User user, @Nonnull Rating rating) {
        players.add(user);
        jskillTeam.addPlayer(user, rating);
    }

    public void leave(@Nonnull User user) {
        players.remove(user);
        jskillTeam.remove(user);
    }

    public boolean contains(@Nonnull User user) {
        return players.stream().anyMatch(i -> user.getUuid().equals(i.getUuid()));
    }

    public jskills.Team getJskillTeam() {
        return this.jskillTeam;
    }

    public int getTeamSize() {
        return this.teamSize;
    }

    public String getName() {
        return this.name;
    }

    public List<User> getPlayers() {
        return this.players;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public Game getGame() {
        return this.game;
    }

    public void setJskillTeam(jskills.Team jskillTeam) {
        this.jskillTeam = jskillTeam;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Team)) return false;
        final Team other = (Team) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$jskillTeam = this.getJskillTeam();
        final Object other$jskillTeam = other.getJskillTeam();
        if (this$jskillTeam == null ? other$jskillTeam != null : !this$jskillTeam.equals(other$jskillTeam))
            return false;
        if (this.getTeamSize() != other.getTeamSize()) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$players = this.getPlayers();
        final Object other$players = other.getPlayers();
        if (this$players == null ? other$players != null : !this$players.equals(other$players)) return false;
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        if (this$color == null ? other$color != null : !this$color.equals(other$color)) return false;
        final Object this$game = this.getGame();
        final Object other$game = other.getGame();
        if (this$game == null ? other$game != null : !this$game.equals(other$game)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $jskillTeam = this.getJskillTeam();
        result = result * PRIME + ($jskillTeam == null ? 43 : $jskillTeam.hashCode());
        result = result * PRIME + this.getTeamSize();
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $players = this.getPlayers();
        result = result * PRIME + ($players == null ? 43 : $players.hashCode());
        final Object $color = this.getColor();
        result = result * PRIME + ($color == null ? 43 : $color.hashCode());
        final Object $game = this.getGame();
        result = result * PRIME + ($game == null ? 43 : $game.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Team;
    }

    public String toString() {
        return "Team(jskillTeam=" + this.getJskillTeam() + ", teamSize=" + this.getTeamSize() + ", name=" + this.getName() + ", players=" + this.getPlayers() + ", color=" + this.getColor() + ", game=" + this.getGame() + ")";
    }
}
