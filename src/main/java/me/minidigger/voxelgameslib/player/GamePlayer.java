package me.minidigger.voxelgameslib.player;

import javax.annotation.Nonnull;
import lombok.Data;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

@Data
public class GamePlayer {

  @Nonnull
  private final Player player;

  public void sendMessage(BaseComponent[] message) {
    player.spigot().sendMessage(message);
  }
}
