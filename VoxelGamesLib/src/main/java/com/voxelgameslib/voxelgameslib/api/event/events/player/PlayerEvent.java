package com.voxelgameslib.voxelgameslib.api.event.events.player;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.event.VoxelGamesLibEvent;
import com.voxelgameslib.voxelgameslib.components.user.User;

public abstract class PlayerEvent extends VoxelGamesLibEvent {

    private User user;

    PlayerEvent(@Nonnull User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
