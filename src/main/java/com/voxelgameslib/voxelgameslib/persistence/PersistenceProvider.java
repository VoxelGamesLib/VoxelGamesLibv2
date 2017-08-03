package com.voxelgameslib.voxelgameslib.persistence;

import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.user.User;

import java.util.Optional;
import java.util.UUID;

/**
 * A persistence provider handles loading and saving of all kinds of data
 */
public interface PersistenceProvider extends Handler {

    /**
     * Called when a provider is started
     */
    void start();

    /**
     * Called when a provider is stopped
     */
    void stop();

    /**
     * Persists the data of a user
     *
     * @param user the user data to persist
     */
    void saveUser(User user);

    /**
     * Tries to load the data for a user based on a uuid
     *
     * @param id the uuid of the user data
     * @return the User, if present
     */
    Optional<User> loadUser(UUID id);
}
