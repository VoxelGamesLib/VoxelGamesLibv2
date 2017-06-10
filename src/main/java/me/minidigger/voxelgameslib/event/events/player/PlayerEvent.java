package me.minidigger.voxelgameslib.event.events.player;

import javax.annotation.Nonnull;
import lombok.Getter;
import me.minidigger.voxelgameslib.event.VoxelGamesLibEvent;
import me.minidigger.voxelgameslib.user.User;

public class PlayerEvent extends VoxelGamesLibEvent {

  @Getter
  private User user;

  PlayerEvent(@Nonnull User user) {
    this.user = user;
  }
}
