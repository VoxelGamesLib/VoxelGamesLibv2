package me.minidigger.voxelgameslib.user;

import com.google.inject.Injector;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import me.minidigger.voxelgameslib.item.Hand;
import me.minidigger.voxelgameslib.item.Item;
import me.minidigger.voxelgameslib.item.ItemBuilder;
import me.minidigger.voxelgameslib.item.Material;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.BaseComponent;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.ComponentBuilder;
import me.minidigger.voxelgameslib.map.Vector3D;
import me.minidigger.voxelgameslib.role.Permission;

/**
 * Abstract implementation of the console user interface that deals with most stuff
 *
 * @param <T> the implementation type
 */
public abstract class AbstractConsoleUser<T> implements ConsoleUser<T> {

  @Inject
  private Injector injector;

  @Nonnull
  @Override
  public UserData getData() {
    throw new UnsupportedOperationException("Console has no data!");
  }

  @Override
  public void setData(UserData data) {
    throw new UnsupportedOperationException("Console has no data!");
  }

  @Nonnull
  @Override
  public BaseComponent[] getDisplayName() {
    return new ComponentBuilder("ConsoleUser").create();
  }

  @Nonnull
  @Override
  public UUID getUuid() {
    return UUID.nameUUIDFromBytes("ConsoleUser".getBytes());
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
