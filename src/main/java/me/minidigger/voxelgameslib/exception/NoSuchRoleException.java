package me.minidigger.voxelgameslib.exception;

import javax.annotation.Nonnull;

/**
 * Thrown when _something_ (a command, most of the time) tries to access a role that does not
 * exist.
 */
public class NoSuchRoleException extends VoxelGameLibException {

  /**
   * @param role the name of the role that was tried to access
   */
  public NoSuchRoleException(@Nonnull String role) {
    super("Could not find role " + role);
  }
}
