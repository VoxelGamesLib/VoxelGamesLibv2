package com.voxelgameslib.voxelgameslib.components.team;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.ChatColor;

import lombok.Data;

import jskills.Rating;

@Data
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
}
