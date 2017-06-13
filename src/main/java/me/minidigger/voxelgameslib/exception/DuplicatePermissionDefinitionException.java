package me.minidigger.voxelgameslib.exception;

import javax.annotation.Nonnull;

import me.minidigger.voxelgameslib.role.Permission;

/**
 * Thrown when something tries to register a new permission for a permission that already exist with
 * a different role
 */
public class DuplicatePermissionDefinitionException extends VoxelGameLibException {

    /**
     * @param permission the already existing permission
     * @param role       the new role
     */
    public DuplicatePermissionDefinitionException(@Nonnull Permission permission,
                                                  @Nonnull String role) {
        super("Tried to register overlapping permission " + permission.getString() + ": ord role: "
                + permission.getRole().getName() + ", new role: " + role);
    }
}
