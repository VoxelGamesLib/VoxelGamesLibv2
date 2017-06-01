package me.minidigger.voxelgameslib.scoreboard;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.user.User;

/**
 * Represents a scoreboard, a sidebar used to display information.
 */
public interface Scoreboard {

  /**
   * @return the title of this scoreboard
   */
  @Nonnull
  String getTitle();

  /**
   * changes the title of the scoreboard.
   *
   * @param title the new title
   */
  void setTitle(@Nonnull String title);

  /**
   * adds a new line to this scoreboard. the key specifies the position, higher key being higher
   * "rank" on the board<br>
   * if there is a line with that key already, it gets overridden!
   *
   * @param key the position of the line
   * @param line the line to add
   */
  void addLine(int key, @Nonnull ScoreboardLine line);

  /**
   * adds a new line to this socreboard, named key for easy access.<br>
   * A new line will always be added on the top.<br>
   * if there is a line with that key already, it gets overridden!
   *
   * @param key the name of this line, for easy access
   * @param line the new line
   * @return the position of the line on the scoreboard
   */
  int addLine(String key, @Nonnull ScoreboardLine line);

  /**
   * Adds a new line to this scoreboard. <br>
   * A new line is will always be added on the top.
   *
   * @param line the new line
   * @return the position of the line on the scoreboard
   */
  int addLine(@Nonnull ScoreboardLine line);

  /**
   * Removes the line with the given key
   *
   * @param key the name of the line to remove
   */
  void removeLine(@Nonnull String key);

  /**
   * Removes the line at the given position
   *
   * @param key the position of the line to remove
   */
  void removeLine(int key);

  /**
   * Removes all lines from this scoreboard
   */
  void removeAllLines();

  /**
   * Return the line at the given position
   *
   * @param key the position of the line to return
   * @return the line at the given position
   */
  @Nonnull
  Optional<ScoreboardLine> getLine(int key);

  /**
   * Returns the line with the given key
   *
   * @param key the name of the line
   * @return the line with the given name
   */
  @Nonnull
  Optional<ScoreboardLine> getLine(@Nonnull String key);

  /**
   * Adds a user to this scoreboard
   *
   * @param user the user to add
   */
  void addUser(@Nonnull User user);

  /**
   * Removes a user from this scoreboard
   *
   * @param user the user to remove
   */
  void removeUser(@Nonnull User user);

  /**
   * Checks if a user was added to this scoreboard
   *
   * @param user the user to check for
   * @return true if the user was added
   */
  boolean isAdded(@Nonnull User user);

  /**
   * @return a list with all users that were added to this scoreboard
   */
  @Nonnull
  List<User> getUsers();

  /**
   * Removes all users from this scoreboard
   */
  void removeAllUsers();


  /**
   * sets the object for the servers implementation.
   *
   * @param object the servers implementation of
   */
  void setImplObject(Object object);

  /**
   * Creates a new simple scoreboard line and adds it automatically
   *
   * @param content the content of the line
   * @return the newly created line
   */
  StringScoreboardLine createAndAddLine(String content);

  /**
   * Creates a new simple scoreboard line and adds it automatically
   *
   * @param content the content of the line
   * @param key the key of the line
   * @return the newly created line
   */
  StringScoreboardLine createAndAddLine(String key, String content);

  /**
   * Creates a new simple scoreboard line and adds it automatically
   *
   * @param content the content of the line
   * @param pos the position of the line
   * @return the newly created line
   */
  StringScoreboardLine createAndAddLine(int pos, String content);
}
