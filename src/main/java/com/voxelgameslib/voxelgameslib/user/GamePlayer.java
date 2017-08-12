package com.voxelgameslib.voxelgameslib.user;

import com.google.gson.annotations.Expose;

import com.voxelgameslib.voxelgameslib.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.elo.RatingWrapper;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.persistence.PersistenceHandler;
import com.voxelgameslib.voxelgameslib.persistence.converter.ComponentConverter;
import com.voxelgameslib.voxelgameslib.persistence.converter.LocaleConverter;
import com.voxelgameslib.voxelgameslib.role.Permission;
import com.voxelgameslib.voxelgameslib.role.Role;
import com.voxelgameslib.voxelgameslib.utils.ChatUtil;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.bukkit.entity.Player;

import jskills.Rating;

/**
 * abstract implementation of the user interface that deals with some stuff
 */
@Entity
@Table(name = "players")
public class GamePlayer implements User {

    @Transient
    @Inject
    private GlobalConfig config;
    @Transient
    @Inject
    private PersistenceHandler persistenceHandler;
    @Transient
    @Inject
    private ChatHandler chatHandler;

    @Transient
    private Player player;

    @Expose
    @Id
    @Type(type = "uuid-char")
    private UUID uuid;

    @Expose
    @Enumerated(EnumType.STRING)
    private Role role = Role.DEFAULT;

    @Expose
    @Convert(converter = LocaleConverter.class)
    private Locale locale = Locale.ENGLISH;

    @Expose
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CollectionTable(name = "ratings")
    @MapKeyColumn(name = "gamemode")
    private Map<String, RatingWrapper> ratings = new HashMap<>();

    @Expose
    @Transient//TODO fix me
    private Map<GameMode, Map<String, Integer>> points = new HashMap<>();

    @Expose
    @Column(name = "display_name")
    @Convert(converter = ComponentConverter.class)
    // combination of <prefix> <rawdisplayname> <suffix>
    private Component displayName;

    @Expose
    @Column(name = "raw_display_name")
    private String rawDisplayName;

    @Expose
    @Convert(converter = ComponentConverter.class)
    private Component prefix = TextComponent.of("");
    @Expose
    @Convert(converter = ComponentConverter.class)
    private Component suffix = TextComponent.of("");

    @Transient
    @Expose//TODO figure out what to do with chat channels
    private List<ChatChannel> channels = new ArrayList<>();
    @Transient
    @Expose
    private ChatChannel activeChannel;

    /* Data to persist. Otherwise has no use */
    @Expose
    @Column(name = "username")
    private String name;
    @Expose
    @Column(name = "ip_address")
    private String ipAddress;
    @Expose
    private boolean banned;

    @Nonnull
    @Override
    public UUID getUuid() {
        return uuid;
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
        if (rawDisplayName == null) {
            // wat?
            rawDisplayName = getPlayer().getDisplayName();
        }

        if (displayName == null) {
            if (prefix == null) {
                prefix = TextComponent.of("");
            }
            if (suffix == null) {
                suffix = TextComponent.of("");
            }
            displayName = TextComponent.of("").append(prefix.append(TextComponent.of(rawDisplayName))).append(suffix);
        }
        return displayName;
    }

    @Override
    public void setDisplayName(@Nonnull String displayName) {
        this.rawDisplayName = displayName;
        refreshDisplayName();
    }

    @Override
    @Nonnull
    public String getRawDisplayName() {
        return rawDisplayName;
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
        return locale;
    }

    @Override
    public void setLocale(@Nonnull Locale locale) {
        this.locale = locale;
    }

    @Override
    @Nonnull
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(@Nonnull Role role) {
        this.role = role;
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
        ratings.put(mode.getName(), new RatingWrapper(rating));
        persistenceHandler.getProvider().saveUser(this);
    }

    @Override
    public void refreshDisplayName() {
        displayName = null; // regenerate full display name
    }

    @Override
    @Nonnull
    public Map<String, RatingWrapper> getRatings() {
        if (ratings == null) {
            ratings = new HashMap<>();
        }
        return ratings;
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
        name = player.getName();
        ipAddress = player.getAddress().getAddress().getHostAddress();
        banned = player.isBanned();
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
        return prefix;
    }

    @Override
    public void setPrefix(@Nonnull Component prefix) {
        this.prefix = prefix;
        refreshDisplayName();
    }

    @Override
    @Nonnull
    public Component getSuffix() {
        return suffix;
    }

    @Override
    public void setSuffix(@Nonnull Component suffix) {
        this.suffix = suffix;
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
}
