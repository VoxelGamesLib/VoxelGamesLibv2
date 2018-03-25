package com.voxelgameslib.voxelgameslib.persistence;

import net.kyori.text.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.persistence.model.UserData;
import com.voxelgameslib.voxelgameslib.stats.Trackable;
import com.voxelgameslib.voxelgameslib.utils.Pair;

/**
 * A persistence provider handles loading and saving of all kinds of data
 */
public interface PersistenceProvider extends Handler {

    /**
     * Called when a provider is started
     */
    @Override
    void enable();

    /**
     * Called when a provider is stopped
     */
    @Override
    void disable();

    /**
     * Persists the data of a user
     *
     * @param user the user data to persist
     */
    void saveUser(@Nonnull UserData user);

    /**
     * Tries to load the data for a user based on a uuid
     *
     * @param id the uuid of the user data
     * @return the UserData, if present
     */
    @Nonnull
    Optional<UserData> loadUser(@Nonnull UUID id);

    List<Pair<Component, Double>> getTop(Trackable type, int amount);
}
