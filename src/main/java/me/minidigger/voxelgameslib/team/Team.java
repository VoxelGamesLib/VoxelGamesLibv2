package me.minidigger.voxelgameslib.team;

import jskills.IPlayer;
import jskills.Rating;
import lombok.Data;
import lombok.Getter;
import me.minidigger.voxelgameslib.user.User;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Data
public class Team  extends jskills.Team {

  private String name = "<unknown>";
  @Getter private List<User> players = new ArrayList<>();

  public void addPlayer(User user, Rating rating) {
    super.addPlayer(user, rating);
    players.add(user);
  }
}
