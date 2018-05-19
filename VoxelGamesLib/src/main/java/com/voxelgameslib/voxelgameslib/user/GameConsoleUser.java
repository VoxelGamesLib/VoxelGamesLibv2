package com.voxelgameslib.voxelgameslib.user;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.elo.RatingWrapper;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.persistence.model.UserData;
import com.voxelgameslib.voxelgameslib.role.Permission;
import com.voxelgameslib.voxelgameslib.role.Role;
import com.voxelgameslib.voxelgameslib.utils.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import jskills.Rating;

/**
 * Abstract implementation of the console user interface that deals with most stuff
 */
public class GameConsoleUser implements ConsoleUser {

    @Inject
    private ChatHandler chatHandler;

    public static final UUID UUID = java.util.UUID.nameUUIDFromBytes("ConsoleUser".getBytes());
    public static final GameConsoleUser INSTANCE = new GameConsoleUser();

    private List<ChatChannel> channels;
    private ChatChannel activeChannel;

    private UserData userData = new UserData();

    public GameConsoleUser(){
        userData.setUuid(UUID);
    }

    @Override
    @Nonnull
    public String getRawDisplayName() {
        return "Console";
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Nonnull
    public Player getPlayer() {
        return null;
    }

    @Override
    public void setPlayer(@Nonnull Player player) {
        // ignore
    }

    @Override
    @Nonnull
    public Locale getLocale() {
        return Locale.ENGLISH;
    }

    @Override
    public void setLocale(@Nonnull Locale locale) {
        // ignore
    }

    @Override
    @Nonnull
    public Role getRole() {
        return Role.ADMIN;
    }

    @Override
    public void setRole(@Nonnull Role role) {
        // ignore
    }

    @Override
    @Nonnull
    public Rating getRating(@Nonnull GameMode mode) {
        return mode.getDefaultRating();
    }

    @Override
    public void saveRating(@Nonnull GameMode mode, @Nonnull Rating rating) {
        // ignore
    }

    @Override
    @Nonnull
    public Map<String, RatingWrapper> getRatings() {
        return new HashMap<>();
    }

    @Override
    public Map<GameMode, Map<String, Integer>> getPoints() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Integer> getPoints(GameMode gameMode) {
        return new HashMap<>();
    }

    @Override
    public int getPoint(GameMode gameMode, String pointId) {
        return 0;
    }

    @Override
    public void setPoint(GameMode gameMode, String pointId, int value) {
        // ignore
    }

    @Override
    public void refreshDisplayName() {
        // ignore
    }

    @Override
    @Nonnull
    public Component getPrefix() {
        return TextComponent.of("");
    }

    @Override
    public void setPrefix(@Nonnull Component prefix) {
        // ignore
    }

    @Override
    @Nonnull
    public Component getSuffix() {
        return TextComponent.of("");
    }

    @Override
    public void setSuffix(@Nonnull Component suffix) {
        // ignore
    }

    @Override
    public void setUuid(@Nonnull UUID uuid) {
        // ignore
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return TextComponent.of("Console");
    }

    @Nonnull
    @Override
    public UUID getUuid() {
        return UUID;
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
    public void setDisplayName(@Nonnull String displayName) {
        // ignore
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

    @Override
    @Nonnull
    public List<ChatChannel> getChannels() {
        return channels;
    }

    @Override
    public void addListeningChannel(@Nonnull String identifier) {
        ChatChannel channel = chatHandler.getChannel(identifier).isPresent() ? chatHandler.getChannel(identifier).get() : null;

        if (channel != null) {
            channels.add(channel);
            channel.addListener(this);
        }
    }

    @Override
    public void removeListeningChannel(@Nonnull String identifier) {
        ChatChannel channel = chatHandler.getChannel(identifier).isPresent() ? chatHandler.getChannel(identifier).get() : null;

        if (channel != null) {
            channels.remove(channel);
            channel.removeListener(this);
        }
    }

    @Override
    @Nonnull
    public ChatChannel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(@Nonnull String identifier) {
        ChatChannel channel = chatHandler.getChannel(identifier).isPresent() ? chatHandler.getChannel(identifier).get() : null;

        if (channel != null) {
            activeChannel = channel;
        }
    }

    @Override
    public void applyRolePrefix() {
        // ignore
    }

    @Override
    public void applyRoleSuffix() {
        // ignore
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public boolean isLeaving() {
        return false;
    }

    @Override
    public void setLeaving(boolean leaving) {
        // ignore
    }
}
