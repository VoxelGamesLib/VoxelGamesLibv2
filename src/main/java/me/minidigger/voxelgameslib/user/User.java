package me.minidigger.voxelgameslib.user;

import java.util.UUID;
import javax.annotation.Nonnull;
import jskills.IPlayer;
import jskills.ISupportPartialPlay;
import jskills.ISupportPartialUpdate;
import me.minidigger.voxelgameslib.block.Direction;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.BaseComponent;
import me.minidigger.voxelgameslib.role.Permission;
import org.bukkit.entity.Player;

/**
 * A Users represents an abstract player of the game. There are implementations for every server mod
 * available.<br>
 */
public interface User extends IPlayer, ISupportPartialPlay,
    ISupportPartialUpdate {

  /**
   * @return a unique identifier for that user.
   */
  @Nonnull
  UUID getUuid();

  /**
   * Send a message to this user.
   *
   * @param message the message to be send
   */
  void sendMessage(@Nonnull BaseComponent... message);

  /**
   * checks if that user has the desired permission.
   *
   * @param perm the permission object to check
   * @return whether or not the user has that permission
   */
  boolean hasPermission(@Nonnull Permission perm);

  /**
   * returns the display name of the user, consists of prefix, name and suffix.
   *
   * @return the display name of the user, consists of prefix, name and suffix.
   */
  BaseComponent[] getDisplayName();

  /**
   * returns the display name of the user, consists of prefix, name and suffix.
   *
   * @return the display name of the user, consists of prefix, name and suffix.
   */
  String getRawDisplayName();

  /**
   * @return the direction the player is looking in
   */
  Direction getFacingDirection();

  /**
   * @return the bukkit player
   */
  Player getPlayer();

}
