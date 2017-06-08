package me.minidigger.voxelgameslib.user;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import jskills.Rating;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.role.Permission;
import me.minidigger.voxelgameslib.role.Role;
import net.kyori.text.BaseComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * abstract implementation of the user interface that deals with some stuff
 */
public class GameUser implements User {

  // TODO this call is a mess, clean it up

  @Inject
  private GlobalConfig config;
  @Inject
  private PersistenceHandler persistenceHandler;

  private BaseComponent[] displayName;

  @Expose
  private User User;

  private Player player;

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

  @Nonnull
  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public void sendMessage(@Nonnull BaseComponent... message) {
    Arrays.stream(message).forEach(msg -> player.sendMessage(msg.toString()));
  }

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
          new TextComponent(getDisplayName()).create(),
          ComponentSerializer.parse(getPrefix()))
          .flatMap(Stream::of)
          .toArray(BaseComponent[]::new);
    }
    return displayName;
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
    return player;
  }

  @Override
  public Locale getLocale() {
    return locale;
  }

  @Override
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  @Override
  public Role getRole() {
    return role;
  }

  @Override
  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public Rating getRating(GameMode mode) {
    Rating rating = ratings.get(mode.getName());
    if (rating == null) {
      rating = mode.getDefaultRating();
      // no need to save here
    }
    return rating;
  }

  @Override
  public void saveRating(GameMode mode, Rating rating) {
    ratings.put(mode.getName(), rating);
    persistenceHandler.getProvider().saveUser(this);
  }

  @Override
  public Map<String, Rating> getRatings() {
    return ratings;
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
