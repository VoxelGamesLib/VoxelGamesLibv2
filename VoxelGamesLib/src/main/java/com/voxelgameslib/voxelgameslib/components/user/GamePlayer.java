package com.voxelgameslib.voxelgameslib.components.user;

import com.google.gson.annotations.Expose;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.Transient;

import com.voxelgameslib.voxelgameslib.api.game.GameMode;
import com.voxelgameslib.voxelgameslib.components.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.components.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.internal.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.components.elo.RatingWrapper;
import com.voxelgameslib.voxelgameslib.internal.lang.Locale;
import com.voxelgameslib.voxelgameslib.internal.persistence.PersistenceHandler;
import com.voxelgameslib.voxelgameslib.internal.persistence.model.UserData;
import com.voxelgameslib.voxelgameslib.api.role.Permission;
import com.voxelgameslib.voxelgameslib.api.role.Role;
import com.voxelgameslib.voxelgameslib.util.utils.ChatUtil;

import org.bukkit.entity.Player;

import jskills.Rating;

/**
 * abstract implementation of the user interface that deals with some stuff
 */
public class GamePlayer implements User {

    @Inject
    private GlobalConfig config;
    @Inject
    private PersistenceHandler persistenceHandler;
    @Inject
    private ChatHandler chatHandler;

    private UUID uuid;
    private Player player;
    private UserData userData;
    private boolean isLeaving;

    @Expose
    @Transient//TODO fix me
    private Map<GameMode, Map<String, Integer>> points = new HashMap<>();

    @Transient
    @Expose//TODO figure out what to do with chat channels
    private List<ChatChannel> channels = new ArrayList<>();
    @Transient
    @Expose
    private ChatChannel activeChannel;

    @Nonnull
    @Override
    public UUID getUuid() {
        return userData.getUuid();
    }

    @Override
    public void sendMessage(@Nonnull Component message) {
        ChatUtil.sendMessage(this, message);
    }

    @Override
    public boolean hasPermission(@Nonnull Permission perm) {
        if (config.useRoleSystem) {
            return getRole().hasPermission(perm);
        }
        return getPlayer().hasPermission(perm.getString());
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        if (userData.getRawDisplayName() == null) {
            // wat?
            userData.setRawDisplayName(getPlayer().getDisplayName());
        }

        if (userData.getDisplayName() == null) {
            if (userData.getPrefix() == null) {
                userData.setPrefix(TextComponent.of(""));
            }
            if (userData.getSuffix() == null) {
                userData.setSuffix(TextComponent.of(""));
            }
            userData.setDisplayName(TextComponent.of("").append(userData.getPrefix().append(TextComponent.of(userData.getRawDisplayName()))).append(userData.getSuffix()));
        }
        return userData.getDisplayName();
    }

    @Override
    public void setDisplayName(@Nonnull String displayName) {
        userData.setRawDisplayName(displayName);
        refreshDisplayName();
    }

    @Override
    @Nonnull
    public String getRawDisplayName() {
        return userData.getRawDisplayName();
    }

    @Override
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(@Nonnull Player player) {
        this.player = player;
        setPlayerData(player);
    }

    @Override
    @Nonnull
    public Locale getLocale() {
        return userData.getLocale();
    }

    @Override
    public void setLocale(@Nonnull Locale locale) {
        userData.setLocale(locale);
    }

    @Override
    @Nonnull
    public Role getRole() {
        return userData.getRole();
    }

    @Override
    public void setRole(@Nonnull Role role) {
        userData.setRole(role);
    }

    @Override
    @Nonnull
    public Rating getRating(@Nonnull GameMode mode) {
        RatingWrapper rating = getRatings().get(mode.getName());
        if (rating == null) {
            return mode.getDefaultRating();
            // no need to save here
        }
        return rating.toRating();
    }

    @Override
    public void saveRating(@Nonnull GameMode mode, @Nonnull Rating rating) {
        userData.getRatings().put(mode.getName(), new RatingWrapper(rating));
        persistenceHandler.getProvider().saveUser(getUserData());
    }

    @Override
    public void refreshDisplayName() {
        userData.setDisplayName(null); // regenerate full display name
    }

    @Override
    @Nonnull
    public Map<String, RatingWrapper> getRatings() {
        if (userData.getRatings() == null) {
            userData.setRatings(new HashMap<>());
        }
        return userData.getRatings();
    }

    @Override
    public Map<GameMode, Map<String, Integer>> getPoints() {
        return points;
    }

    @Override
    public Map<String, Integer> getPoints(GameMode gameMode) {
        return points.get(gameMode);
    }

    @Override
    public int getPoint(GameMode gameMode, String pointId) {
        return points.get(gameMode).get(pointId);
    }

    @Override
    public void setPoint(GameMode gameMode, String pointId, int value) {
        Integer point = points.get(gameMode).get(pointId);

        if (point == null) {
            throw new RuntimeException("Requested point " + pointId + " but does not exist.");
        } else {
            points.get(gameMode).put(pointId, value);
        }
    }

    private void setPlayerData(@Nonnull Player player) {
        userData.setName(player.getName());
        userData.setIpAddress(player.getAddress().getAddress().getHostAddress());
        userData.setBanned(player.isBanned());
    }

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
    public Component getPrefix() {
        return userData.getPrefix();
    }

    @Override
    public void setPrefix(@Nonnull Component prefix) {
        userData.setPrefix(prefix);
        refreshDisplayName();
    }

    @Override
    @Nonnull
    public Component getSuffix() {
        return userData.getSuffix();
    }

    @Override
    public void setSuffix(@Nonnull Component suffix) {
        userData.setSuffix(suffix);
        refreshDisplayName();
    }

    @Override
    public void setUuid(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    @Nonnull
    public List<ChatChannel> getChannels() {
        return channels;
    }

    @Override
    public void addListeningChannel(@Nonnull String identifier) {
        Optional<ChatChannel> channel = chatHandler.getChannel(identifier);

        if (channel.isPresent()) {
            channels.add(channel.get());
            channel.get().addListener(this);
        }
    }

    @Override
    public void removeListeningChannel(@Nonnull String identifier) {
        ChatChannel channel = chatHandler.getChannel(identifier).isPresent() ? chatHandler.getChannel(identifier).get() : null;

        if (channel != null) {
            channels.remove(channel);
            channel.removeListener(this);

            if (channel == activeChannel) {
                activeChannel = chatHandler.defaultChannel;
            }
        }
    }

    @Override
    @Nonnull
    public ChatChannel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(@Nonnull String identifier) {
        chatHandler.getChannel(identifier).ifPresent((chatChannel -> activeChannel = chatChannel));
    }

    @Override
    public void applyRolePrefix() {
        if (config.useRoleSystem && getRole().getPrefix() != null) {
            setPrefix(getRole().getPrefix());
        }
    }

    @Override
    public void applyRoleSuffix() {
        if (config.useRoleSystem && getRole().getSuffix() != null) {
            setSuffix(getRole().getSuffix());
        }
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
        return isLeaving;
    }

    @Override
    public void setLeaving(boolean leaving) {
        this.isLeaving = leaving;
    }
}
