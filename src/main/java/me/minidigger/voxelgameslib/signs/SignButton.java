package me.minidigger.voxelgameslib.signs;

import me.minidigger.voxelgameslib.block.Block;
import me.minidigger.voxelgameslib.user.User;

/**
 * Represents a sign a user can press to cause a action
 */
public interface SignButton {

  /**
   * Executes the action associated with this button
   *
   * @param user the user that pressed this sign
   * @param block the block of this sign
   */
  void execute(User user, Block block);
}
