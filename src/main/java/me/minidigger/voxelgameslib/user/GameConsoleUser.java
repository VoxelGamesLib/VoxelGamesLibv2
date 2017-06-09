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
import me.minidigger.voxelgameslib.utils.ChatUtil;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Abstract implementation of the console user interface that deals with most stuff
 */
public class GameConsoleUser implements ConsoleUser {

  @Override
  public String getRawDisplayName() {
    return "Console";
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
// ignore
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

  @Override
  public Component getPrefix() {
    return new TextComponent("");
  }

  @Override
  public Component getSuffix() {
    return new TextComponent("");
  }

  @Override
  public void setPrefix(Component prefix) {
// ignore
  }

  @Override
  public void setSuffix(Component suffix) {
// ignore
  }

  @Override
  public void setUuid(UUID uuid) {
// ignore
  }

  @Nonnull
  @Override
  public UUID getUuid() {
    return UUID.nameUUIDFromBytes("ConsoleUser".getBytes());
  }

  @Override
  public void sendMessage(@Nonnull Component message) {
    Bukkit.getConsoleSender().sendMessage("[VGL] " + ChatUtil.toPlainText(message));
  }

  @Override
  public boolean hasPermission(@Nonnull Permission perm) {
    return true;
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent("Console");
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
