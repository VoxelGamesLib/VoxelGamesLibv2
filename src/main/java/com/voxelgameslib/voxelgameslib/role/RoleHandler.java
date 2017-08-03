package com.voxelgameslib.voxelgameslib.role;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.voxelgameslib.voxelgameslib.exception.DuplicatePermissionDefinitionException;
import com.voxelgameslib.voxelgameslib.exception.NoSuchRoleException;
import com.voxelgameslib.voxelgameslib.handler.Handler;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles all roles for this server. The idea of roles is that server admins don't need to deal
 * with permissions because all permissions are organized under roles by default
 */
@Singleton
public class RoleHandler implements Handler {

    @Inject
    private Injector injector;

    private final List<Permission> knownPermissions = new ArrayList<>();

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        knownPermissions.clear();
    }

    /**
     * Tries to register a new permission object. will return existing one instead of duplicating.
     *
     * @param perm the perm string
     * @param role the role name
     * @return the permission object that belongs to the given input
     * @throws NoSuchRoleException when a role is not registered
     */
    @Nonnull
    public Permission registerPermission(@Nonnull String perm, @Nonnull String role) {
        Optional<Role> r = getRole(role);
        if (!r.isPresent()) {
            throw new NoSuchRoleException(role);
        }

        Optional<Permission> opt = getPermission(perm);
        if (opt.isPresent()) {
            if (!opt.get().getRole().getName().equalsIgnoreCase(role)) {
                throw new DuplicatePermissionDefinitionException(opt.get(), role);
            }
        }

        Permission p = new Permission(perm, r.get());
        knownPermissions.add(p);
        return p;
    }

    /**
     * Searches for a permission object with that permission string
     *
     * @param perm the permission string to search for
     * @return the optional search result
     */
    @Nonnull
    public Optional<Permission> getPermission(@Nonnull String perm) {
        return knownPermissions.stream().filter(p -> p.getString().equalsIgnoreCase(perm)).findAny();
    }

    /**
     * Searches for a role with that name
     *
     * @param role the name of the role to search for
     * @return the optional search result
     */
    @Nonnull
    public Optional<Role> getRole(@Nonnull String role) {
        try {
            return Optional.of(Role.fromName(role));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
