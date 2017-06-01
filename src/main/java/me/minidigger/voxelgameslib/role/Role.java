package me.minidigger.voxelgameslib.role;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A role is part of the simplified permission system. all permissions are assigned to roles by
 * default so there is no permission setup needed.
 */
public enum Role {

  DEFAULT("default", null),
  PREMIUM("premium", DEFAULT),
  MODERATOR("moderator", PREMIUM),
  ADMIN("admin", MODERATOR);

  @Nonnull
  private final String name;
  @Nullable
  private final Role parent;

  Role(@Nonnull String name, @Nullable Role parent) {
    this.name = name;
    this.parent = parent;
  }

  /**
   * @return the name of the role
   */
  @Nonnull
  public String getName() {
    return name;
  }

  /**
   * @return the parent role, can be null if there is no parent (like for the default role)
   */
  @Nullable
  public Role getParent() {
    return parent;
  }

  /**
   * Checks if this role has the desired permission. Walks up the inheritance tree.
   *
   * @param perm the permission to check
   * @return if the role has that permission
   */
  public boolean hasPermission(@Nonnull Permission perm) {
    String roleName = perm.getRole().getName();
    Role currRole = this;
    while (currRole != null) {
      if (currRole.getName().equalsIgnoreCase(roleName)) {
        return true;
      }

      currRole = currRole.getParent();
    }

    return false;
  }

  /**
   * Searches for the role with the given. throws a illegal argument exception if the role doesn't
   * exist.
   *
   * @param name the name to search for
   * @return the role that was found.
   */
  public static Role fromName(String name) {
    for (Role role : values()) {
      if (role.getName().equalsIgnoreCase(name)) {
        return role;
      }
    }
    throw new IllegalArgumentException();
  }
}
