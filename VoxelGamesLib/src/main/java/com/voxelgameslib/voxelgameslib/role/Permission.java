package com.voxelgameslib.voxelgameslib.role;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * A permission is the ability to do something. a user can get a permission to do something via his role or manually via
 * the string.
 */
public class Permission {

    @Inject
    private static RoleHandler roleHandler;

    @Nonnull
    private final String string;
    @Nonnull
    private final Role role;

    Permission(@Nonnull String string, @Nonnull Role role) {
        this.string = string;
        this.role = role;
    }

    /**
     * @return the string representation of this permission
     */
    @Nonnull
    public String getString() {
        return string;
    }

    /**
     * @return the role that has this permission by default
     */
    @Nonnull
    public Role getRole() {
        return role;
    }

    /**
     * Convenience method to statically register a permission
     *
     * @param perm the permission string to register
     * @param role the role this permission defaults to
     * @return the new permission object
     */
    @Nonnull
    public static Permission register(@Nonnull String perm, @Nonnull Role role) {
        return roleHandler.registerPermission(perm, role.getName());
    }
}
