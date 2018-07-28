package com.voxelgameslib.voxelgameslib.persistence;

import net.kyori.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.persistence.model.GameData;
import com.voxelgameslib.voxelgameslib.persistence.model.UserData;
import com.voxelgameslib.voxelgameslib.stats.Trackable;
import com.voxelgameslib.voxelgameslib.utils.Pair;

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

    @Override
    public List<Pair<Component, Double>> getTopWithName(Trackable type, int amount) {
        return new ArrayList<>();
    }

    @Override
    public List<Pair<UUID, Double>> getTopWithUUID(Trackable type, int amount) {
        return new ArrayList<>();
    }

    @Override
    public void saveGame(GameData gameData) {
        // ignore
    }
}
