package me.minidigger.voxelgameslib.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.exception.ScoreboardException;
import me.minidigger.voxelgameslib.user.User;

/**
 * Abstract implementation of a scoreboard, implements some limited functionality
 */
public abstract class AbstractScoreboard implements Scoreboard {

  private List<User> users = new ArrayList<>();
  private Map<Integer, ScoreboardLine> lines = new HashMap<>();
  private Map<String, Integer> keys = new HashMap<>();
  private int nextPos = 1;

  @Override
  public void addLine(int key, @Nonnull ScoreboardLine line) {
    lines.put(key, line);
  }

  @Override
  public int addLine(@Nonnull ScoreboardLine line) {
    int pos = nextPos++;
    while (getLine(pos).isPresent()) {
      pos = nextPos++;
    }
    addLine(pos, line);
    return pos;
  }

  @Override
  public int addLine(String key, @Nonnull ScoreboardLine line) {
    int pos = addLine(line);
    keys.put(key, pos);
    return pos;
  }

  @Override
  public void removeLine(@Nonnull String key) {
    Integer pos = keys.get(key);
    if (pos == null) {
      throw new ScoreboardException("Can't remove line with unknown key " + key);
    }

    removeLine(pos);
  }

  @Override
  public void removeLine(int key) {
    lines.remove(key);
  }

  @Override
  public void removeAllLines() {
    new HashSet<>(lines.keySet()).forEach(this::removeLine);
  }

  @Nonnull
  @Override
  public Optional<ScoreboardLine> getLine(int key) {
    return Optional.ofNullable(lines.get(key));
  }

  @Nonnull
  @Override
  public Optional<ScoreboardLine> getLine(@Nonnull String key) {
    Integer pos = keys.get(key);
    if (pos == null) {
      return Optional.empty();
    }

    return getLine(pos);
  }

  @Override
  public void addUser(@Nonnull User user) {
    if (!isAdded(user)) {
      users.add(user);
    }
  }

  @Override
  public void removeUser(@Nonnull User user) {
    users.remove(user);
  }

  @Override
  public boolean isAdded(@Nonnull User user) {
    return users.contains(user);
  }

  @Nonnull
  @Override
  public List<User> getUsers() {
    return users;
  }

  @Override
  public void removeAllUsers() {
    new ArrayList<>(users).forEach(this::removeUser);
  }
}
