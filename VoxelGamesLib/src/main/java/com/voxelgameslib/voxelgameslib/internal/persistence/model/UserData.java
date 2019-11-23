package com.voxelgameslib.voxelgameslib.internal.persistence.model;

import com.google.gson.annotations.Expose;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
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

import com.voxelgameslib.voxelgameslib.components.elo.RatingWrapper;
import com.voxelgameslib.voxelgameslib.internal.persistence.converter.ComponentConverter;
import com.voxelgameslib.voxelgameslib.internal.lang.Locale;
import com.voxelgameslib.voxelgameslib.internal.persistence.converter.LocaleConverter;
import com.voxelgameslib.voxelgameslib.internal.persistence.converter.TrackableConverter;
import com.voxelgameslib.voxelgameslib.api.role.Role;
import com.voxelgameslib.voxelgameslib.api.stats.StatInstance;
import com.voxelgameslib.voxelgameslib.api.stats.Trackable;

@Entity
@Table(name = "players")
public class UserData {

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
    @Column(name = "raw_display_name")
    private String rawDisplayName;

    @Expose
    @Column(name = "display_name")
    @Convert(converter = ComponentConverter.class)
    private Component displayName;

    @Expose
    @Convert(converter = ComponentConverter.class)
    private Component prefix = TextComponent.of("");
    @Expose
    @Convert(converter = ComponentConverter.class)
    private Component suffix = TextComponent.of("");

    @Expose
    @Column(name = "username")
    private String name;
    @Expose
    @Column(name = "ip_address")
    private String ipAddress;
    @Expose
    private boolean banned;

    @Expose
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CollectionTable(name = "stats")
    @MapKeyColumn(name = "stat_type")
    @Convert(converter = TrackableConverter.class, attributeName = "key")
    private Map<Trackable, StatInstance> stats = new HashMap<>();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Map<String, RatingWrapper> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, RatingWrapper> ratings) {
        this.ratings = ratings;
    }

    public String getRawDisplayName() {
        return rawDisplayName;
    }

    public void setRawDisplayName(String rawDisplayName) {
        this.rawDisplayName = rawDisplayName;
    }

    @Nullable
    public Component getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    public Component getPrefix() {
        return prefix;
    }

    public void setPrefix(Component prefix) {
        this.prefix = prefix;
    }

    public Component getSuffix() {
        return suffix;
    }

    public void setSuffix(Component suffix) {
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Map<Trackable, StatInstance> getStats() {
        return stats;
    }

    public StatInstance getStat(Trackable type) {
        return stats.computeIfAbsent(type, t -> t.getNewInstance(uuid));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return banned == userData.banned &&
                Objects.equals(uuid, userData.uuid) &&
                role == userData.role &&
                Objects.equals(locale, userData.locale) &&
                Objects.equals(ratings, userData.ratings) &&
                Objects.equals(rawDisplayName, userData.rawDisplayName) &&
                Objects.equals(displayName, userData.displayName) &&
                Objects.equals(prefix, userData.prefix) &&
                Objects.equals(suffix, userData.suffix) &&
                Objects.equals(name, userData.name) &&
                Objects.equals(ipAddress, userData.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, role, locale, ratings, rawDisplayName, displayName, prefix, suffix, name, ipAddress, banned);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "uuid=" + uuid +
                ", role=" + role +
                ", locale=" + locale +
                ", ratings=" + ratings +
                ", rawDisplayName='" + rawDisplayName + '\'' +
                ", displayName=" + displayName +
                ", prefix=" + prefix +
                ", suffix=" + suffix +
                ", name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", banned=" + banned +
                '}';
    }
}
