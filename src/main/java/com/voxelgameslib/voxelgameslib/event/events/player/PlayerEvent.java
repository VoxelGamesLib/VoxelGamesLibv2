package com.voxelgameslib.voxelgameslib.event.events.player;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.event.VoxelGamesLibEvent;
import com.voxelgameslib.voxelgameslib.user.User;

import lombok.Getter;

public abstract class PlayerEvent extends VoxelGamesLibEvent {

    @Getter
    private User user;

    PlayerEvent(@Nonnull User user) {
        this.user = user;
    }
}
