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
import jskills.Rating;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.bukkit.entity.Player;
import org.hibernate.annotations.Type;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.*;

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
    public Component getDisplayName() {
        if (rawDisplayName == null) {
            // wat?
            rawDisplayName = getPlayer().getDisplayName();
        }

        if (displayName == null && rawDisplayName != null) {
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
    public String getRawDisplayName() {
        return rawDisplayName;
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
        RatingWrapper rating = getRatings().get(mode.getName());
        if (rating == null) {
            return mode.getDefaultRating();
            // no need to save here
        }
        return rating.toRating();
    }

    @Override
    public void saveRating(GameMode mode, Rating rating) {
        ratings.put(mode.getName(), new RatingWrapper(rating));
        persistenceHandler.getProvider().saveUser(this);
    }

    @Override
    public Map<String, RatingWrapper> getRatings() {
        if (ratings == null) {
            ratings = new HashMap<>();
        }
        return ratings;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
        setPlayerData(player);
    }

    @Override
    public void refreshDisplayName() {
        displayName = null; // regenerate full display name
    }

    private void setPlayerData(Player player) {
        name = player.getName();
        ipAddress = player.getAddress().getAddress().getHostAddress();
        banned = player.isBanned();
    }

    @Override
    public void setDisplayName(String displayName) {
        this.rawDisplayName = displayName;
        refreshDisplayName();
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
    public Component getPrefix() {
        return prefix;
    }

    @Override
    public Component getSuffix() {
        return suffix;
    }

    @Override
    public void setPrefix(Component prefix) {
        this.prefix = prefix;
        refreshDisplayName();
    }

    @Override
    public void setSuffix(Component suffix) {
        this.suffix = suffix;
        refreshDisplayName();
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public List<ChatChannel> getChannels() {
        return channels;
    }

    @Override
    public void addListeningChannel(String identifier) {
        Optional<ChatChannel> channel = chatHandler.getChannel(identifier);

        if (channel.isPresent()) {
            channels.add(channel.get());
            channel.get().addListener(this);
        }
    }

    @Override
    public void removeListeningChannel(String identifier) {
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
    public ChatChannel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(String identifier) {
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
