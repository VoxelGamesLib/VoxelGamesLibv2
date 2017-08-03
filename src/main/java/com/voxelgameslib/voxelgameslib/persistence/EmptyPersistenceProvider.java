package com.voxelgameslib.voxelgameslib.persistence;

import com.voxelgameslib.voxelgameslib.user.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Empty persistence provider, used when persistence is disabled
 */
public class EmptyPersistenceProvider implements PersistenceProvider {

    @Override
    public void start() {
        // ignore
    }

    @Override
    public void stop() {
        // ignore
    }

    @Override
    public void saveUser(User user) {
        // ignore
    }

    @Override
    public Optional<User> loadUser(UUID id) {
        return Optional.empty();
    }
}
