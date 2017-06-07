package me.minidigger.voxelgameslib.user;

import com.google.inject.Injector;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import me.minidigger.voxelgameslib.role.Permission;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Abstract implementation of the console user interface that deals with most stuff
 */
public class GameConsoleUser implements ConsoleUser {

  @Nonnull
  @Override
  public BaseComponent[] getDisplayName() {
    return new ComponentBuilder("ConsoleUser").create();
  }

  @Override
  public String getRawDisplayName() {
    return null;
  }

  @Override
  public BlockFace getFacingDirection() {
    return null;
  }

  @Override
  public Player getPlayer() {
    return null;
  }

  @Nonnull
  @Override
  public UUID getUuid() {
    return UUID.nameUUIDFromBytes("ConsoleUser".getBytes());
  }

  @Override
  public void sendMessage(@Nonnull BaseComponent... message) {

  }

  @Override
  public boolean hasPermission(@Nonnull Permission perm) {
    return true;
  }

  /* elo stuff */
  @Override
  public double getPartialPlayPercentage() {
    return 1.0;
  }

  @Override
  public double getPartialUpdatePercentage() {
    return 1.0;
  }
}
