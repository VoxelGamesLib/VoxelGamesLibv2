package com.voxelgameslib.voxelgameslib.api.stats;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.api.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.components.user.User;
import com.voxelgameslib.voxelgameslib.components.user.UserHandler;

import org.bukkit.event.Listener;

public abstract class Stat {

    @Inject
    private StatsHandler statsHandler;
    @Inject
    private UserHandler userHandler;

    @Nullable
    public Listener getListener() {
        return null;
    }

    public void tickOneMinute() {
        //NOP
    }

    public boolean shouldTick() {
        return false;
    }

    public double defaultValue() {
        return 0.0;
    }

    public StatInstance getInstance(User user) {
        return user.getUserData().getStat(getType());
    }

    public StatInstance getInstance(UUID id) {
        return getInstance(getUser(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat stat = (Stat) o;
        return Objects.equals(getType(), stat.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    public abstract Trackable getType();

    public StatInstance getNewInstance(UUID uuid) {
        return new StatInstance(getUser(uuid), getType(), defaultValue());
    }

    public User getUser(UUID uuid) {
        return userHandler.getUser(uuid).orElseThrow(() -> new VoxelGameLibException("Unknown user " + uuid));
    }
}
