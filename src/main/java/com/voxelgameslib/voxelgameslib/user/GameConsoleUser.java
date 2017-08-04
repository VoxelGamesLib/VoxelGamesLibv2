package com.voxelgameslib.voxelgameslib.user;

import com.voxelgameslib.voxelgameslib.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.elo.RatingWrapper;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.role.Permission;
import com.voxelgameslib.voxelgameslib.role.Role;
import com.voxelgameslib.voxelgameslib.utils.ChatUtil;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;

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
    public Map<String, RatingWrapper> getRatings() {
        return new HashMap<>();
    }

    @Override
    public void setPlayer(Player player) {
        // ignore
    }

    @Override
    public void refreshDisplayName() {
        // ignore
    }

    @Override
    public void setDisplayName(String displayName) {
        // ignore
    }

    @Override
    public Component getPrefix() {
        return TextComponent.of("");
    }

    @Override
    public Component getSuffix() {
        return TextComponent.of("");
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
    public Component getDisplayName() {
        return TextComponent.of("Console");
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
    public List<ChatChannel> getChannels() {
        return channels;
    }

    @Override
    public void addListeningChannel(String identifier) {
        ChatChannel channel = chatHandler.getChannel(identifier).isPresent() ? chatHandler.getChannel(identifier).get() : null;

        if (channel != null) {
            channels.add(channel);
            channel.addListener(this);
        }
    }

    @Override
    public void removeListeningChannel(String identifier) {
        ChatChannel channel = chatHandler.getChannel(identifier).isPresent() ? chatHandler.getChannel(identifier).get() : null;

        if (channel != null) {
            channels.remove(channel);
            channel.removeListener(this);
        }
    }

    @Override
    public ChatChannel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(String identifier) {
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
}
