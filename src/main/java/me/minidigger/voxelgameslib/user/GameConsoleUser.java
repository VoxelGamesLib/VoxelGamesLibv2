package me.minidigger.voxelgameslib.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import jskills.Rating;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.role.Permission;
import me.minidigger.voxelgameslib.role.Role;
import net.kyori.text.BaseComponent;
import net.kyori.text.TextComponent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Abstract implementation of the console user interface that deals with most stuff
 */
public class GameConsoleUser implements ConsoleUser {

  @Nonnull
  @Override
  public BaseComponent[] getDisplayName() {
    // todo: this really doesn't need to be an array... but did this anyway, another compile error fixed
    BaseComponent[] components = new TextComponent[1];
    components[0] = new TextComponent("Console");
    return components;
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

  @Override
  public Locale getLocale() {
    return Locale.ENGLISH;
  }

  @Override
  public void setLocale(Locale locale) {
// ignore
  }

  @Override
  public Role getRole() {
    return Role.ADMIN;
  }

  @Override
  public void setRole(Role role) {
    // ignore
  }

  @Override
  public Rating getRating(GameMode mode) {
    return mode.getDefaultRating();
  }

  @Override
  public void saveRating(GameMode mode, Rating rating) {

  }

  @Override
  public Map<String, Rating> getRatings() {
    return new HashMap<>();
  }

  @Override
  public void setPlayer(Player player) {
// ignore
  }

  @Override
  public void setDisplayName(String displayName) {
// ignore
  }

  @Nonnull
  @Override
  public UUID getUuid() {
    return UUID.nameUUIDFromBytes("ConsoleUser".getBytes());
  }

  @Override
  public void sendMessage(@Nonnull BaseComponent... message) {
//TODO GameConsoleUser#sendMessage
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
