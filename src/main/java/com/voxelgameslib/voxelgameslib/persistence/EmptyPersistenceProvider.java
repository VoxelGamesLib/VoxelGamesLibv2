package com.voxelgameslib.voxelgameslib.persistence;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.user.User;

/**
 * Empty persistence provider, used when persistence is disabled
 */
public class EmptyPersistenceProvider implements PersistenceProvider {

    @Override
    public void enable() {
        // ignore
    }

    @Override
    public void disable() {
        // ignore
    }

    @Override
    public void saveUser(@Nonnull User user) {
        // ignore
    }

    @Override
    @Nonnull
    public Optional<User> loadUser(@Nonnull UUID id) {
        return Optional.empty();
    }
}
