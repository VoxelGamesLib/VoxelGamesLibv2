package me.minidigger.voxelgameslib.persistence;

import java.util.Optional;
import java.util.UUID;

import me.minidigger.voxelgameslib.user.User;

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
