package com.voxelgameslib.voxelgameslib.stats;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.user.UserHandler;

import org.bukkit.event.Listener;

public abstract class Stat {

    @Inject
    private StatsHandler statsHandler;
    @Inject
    private UserHandler userHandler;

    public abstract String getName();

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
        return statsHandler.getInstance(this, user);
    }

    public StatInstance getInstance(UUID id) {
        return statsHandler.getInstance(this, userHandler.getUser(id).orElseThrow(() -> new VoxelGameLibException("Unknown user " + id)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat stat = (Stat) o;
        return Objects.equals(getName(), stat.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
