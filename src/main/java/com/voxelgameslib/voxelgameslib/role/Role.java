package com.voxelgameslib.voxelgameslib.role;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;

/**
 * A role is part of the simplified permission system. all permissions are assigned to roles by
 * default so there is no permission setup needed.
 */
public enum Role {

    DEFAULT("default", null, null, null),
    PREMIUM("premium", DEFAULT, TextComponent.of("[PREMIUM] ").color(TextColor.GREEN), null),
    MODERATOR("moderator", PREMIUM, TextComponent.of("[MOD] ").color(TextColor.AQUA), null),
    ADMIN("admin", MODERATOR, TextComponent.of("[ADMIN] ").color(TextColor.RED), null);

    @Nonnull
    private final String name;
    @Nullable
    private final Role parent;
    @Nullable
    @Getter
    private final Component prefix;
    @Nullable
    @Getter
    private final Component suffix;

    Role(@Nonnull String name, @Nullable Role parent, @Nullable Component prefix, @Nullable Component suffix) {
        this.name = name;
        this.parent = parent;
        this.prefix = prefix;
        this.suffix = suffix;
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
