package me.minidigger.voxelgameslib.user;

import com.google.inject.Injector;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.BaseComponent;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.ComponentBuilder;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.chat.ComponentSerializer;
import me.minidigger.voxelgameslib.role.Permission;

/**
 * abstract implementation of the user interface that deals with some stuff
 */
public abstract class AbstractUser implements User {

  private UserData userData;

  @Inject
  private GlobalConfig config;
  @Inject
  private Injector injector;

  private BaseComponent[] displayName;

  @Override
  public boolean hasPermission(@Nonnull Permission perm) {
    if (config.useRoleSystem) {
      return getData().getRole().hasPermission(perm);
    }
    return false;
  }

  @Override
  public BaseComponent[] getDisplayName() {
    if (displayName == null) {
      displayName = Stream.of(ComponentSerializer.parse(getData().getPrefix()),
          new ComponentBuilder(getData().getDisplayName()).create(),
          ComponentSerializer.parse(getData().getPrefix()))
          .flatMap(Stream::of)
          .toArray(BaseComponent[]::new);
    }
    return displayName;
  }

  @Override
  public double getPartialPlayPercentage() {
    return 1.0;
  }

  @Override
  public double getPartialUpdatePercentage() {
    return 1.0;
  }

}
