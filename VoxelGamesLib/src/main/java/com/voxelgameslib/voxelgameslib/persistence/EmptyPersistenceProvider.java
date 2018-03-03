package com.voxelgameslib.voxelgameslib.persistence;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.persistence.model.UserData;
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
    public void saveUser(@Nonnull UserData user) {
        // ignore
    }

    @Override
    @Nonnull
    public Optional<UserData> loadUser(@Nonnull UUID id) {
        return Optional.empty();
    }
}
