package com.voxelgameslib.voxelgameslib.persistence;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.user.User;

/**
 * A persistence provider handles loading and saving of all kinds of data
 */
public interface PersistenceProvider extends Handler {

    /**
     * Called when a provider is started
     */
    void enable();

    /**
     * Called when a provider is stopped
     */
    void disable();

    /**
     * Persists the data of a user
     *
     * @param user the user data to persist
     */
    void saveUser(@Nonnull User user);

    /**
     * Tries to load the data for a user based on a uuid
     *
     * @param id the uuid of the user data
     * @return the User, if present
     */
    @Nonnull
    Optional<User> loadUser(@Nonnull UUID id);
}
