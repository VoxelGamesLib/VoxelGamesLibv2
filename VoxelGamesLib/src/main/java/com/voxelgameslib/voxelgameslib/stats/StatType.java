package com.voxelgameslib.voxelgameslib.stats;

import java.util.UUID;

import com.voxelgameslib.voxelgameslib.user.User;

public enum StatType {

    JOIN_COUNT,
    PLAY_TIME;

    private Stat stat;

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public Stat getStat() {
        return stat;
    }

    public StatInstance getInstance(User user) {
        return stat.getInstance(user);
    }

    public StatInstance getInstance(UUID id) {
        return stat.getInstance(id);
    }

    public StatInstance getNewInstance(UUID uuid) {
        return stat.getNewInstance(uuid);
    }

    // workaround for not having access to the user handler
    User getUser(UUID id) {
       return stat.getUser(id);
    }
}
