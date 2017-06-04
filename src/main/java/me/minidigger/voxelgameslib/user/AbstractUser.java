package me.minidigger.voxelgameslib.user;

import com.google.gson.annotations.Expose;
import com.google.inject.Injector;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import jskills.Rating;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.role.Role;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import me.minidigger.voxelgameslib.role.Permission;

/**
 * abstract implementation of the user interface that deals with some stuff
 */
public abstract class AbstractUser implements User {

  private UserData userData;

  @Inject
  private GlobalConfig config;

  private BaseComponent[] displayName;

  @Expose
  private UUID uuid;

  @Expose
  private Role role = Role.DEFAULT;

  @Expose
  private Locale locale = Locale.ENGLISH;

  @Expose
  private Map<String, Rating> ratings = new HashMap<>();

  @Expose
  private BaseComponent[] prefix;
  @Expose
  private BaseComponent[] suffix;

  @Override
  public boolean hasPermission(@Nonnull Permission perm) {
    if (config.useRoleSystem) {
      return getRole().hasPermission(perm);
    }
    return false;
  }

  @Override
  public BaseComponent[] getDisplayName() {
    if (displayName == null) {
      displayName = Stream.of(ComponentSerializer.parse(getPrefix()),
          new ComponentBuilder(getDisplayName()).create(),
          ComponentSerializer.parse(getPrefix()))
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
