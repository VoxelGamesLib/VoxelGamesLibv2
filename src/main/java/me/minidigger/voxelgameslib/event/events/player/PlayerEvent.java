package me.minidigger.voxelgameslib.event.events.player;

import javax.annotation.Nonnull;

import me.minidigger.voxelgameslib.event.VoxelGamesLibEvent;
import me.minidigger.voxelgameslib.user.User;

import lombok.Getter;

public abstract class PlayerEvent extends VoxelGamesLibEvent {

    @Getter
    private User user;

    PlayerEvent(@Nonnull User user) {
        this.user = user;
    }
}
