package me.minidigger.voxelgameslib.components.team;

import java.util.ArrayList;
import java.util.List;

import me.minidigger.voxelgameslib.user.User;

import jskills.Rating;
import lombok.Data;
import lombok.Getter;

@Data
public class Team extends jskills.Team {

    private String name = "<unknown>";
    @Getter
    private List<User> players = new ArrayList<>();

    public void addPlayer(User user, Rating rating) {
        super.addPlayer(user, rating);
        players.add(user);
    }
}
