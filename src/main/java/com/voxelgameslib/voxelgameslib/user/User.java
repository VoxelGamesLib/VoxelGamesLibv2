package com.voxelgameslib.voxelgameslib.user;

import com.voxelgameslib.voxelgameslib.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.elo.RatingWrapper;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.role.Permission;
import com.voxelgameslib.voxelgameslib.role.Role;

import net.kyori.text.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import jskills.IPlayer;
import jskills.ISupportPartialPlay;
import jskills.ISupportPartialUpdate;
import jskills.Rating;

/**
 * A Users represents an abstract player of the game. There are implementations for every server mod
 * available.<br>
 */
public interface User extends IPlayer, ISupportPartialPlay, ISupportPartialUpdate {

    /**
     * @return a unique identifier for that user.
     */
    @Nonnull
    UUID getUuid();

    /**
     * Send a message to this user.
     *
     * @param message the message to be send
     */
    void sendMessage(@Nonnull Component message);

    /**
     * checks if that user has the desired permission.
     *
     * @param perm the permission object to check
     * @return whether or not the user has that permission
     */
    boolean hasPermission(@Nonnull Permission perm);

    /**
     * returns the display name of the user, consists of prefix, raw display name and suffix.
     *
     * @return the display name of the user, consists of prefix, raw display name and suffix.
     */
    Component getDisplayName();

    /**
     * returns the display name of the user
     *
     * @return the display name of the user
     */
    String getRawDisplayName();

    /**
     * @return the bukkit player
     */
    Player getPlayer();

    /**
     * @return the players currently selected locale
     */
    Locale getLocale();

    /**
     * sets the players locale. locale doesn't need to be installed, will default to default server
     * locale
     *
     * @param locale the new locale
     */
    void setLocale(Locale locale);

    /**
     * @return the role of that player
     */
    Role getRole();

    /**
     * changes the role of the user
     *
     * @param role the new role
     */
    void setRole(Role role);

    /**
     * @param mode the mode to get the rating for
     * @return the rating of this player for gamemode mode. will return default values if not
     * present
     */
    Rating getRating(GameMode mode);

    /**
     * Saves a rating for this users. will override existing ratings
     *
     * @param mode   the mode the rating was achieved in
     * @param rating the new rating
     */
    void saveRating(GameMode mode, Rating rating);

    /**
     * @return all ratings for this player
     */
    Map<String, RatingWrapper> getRatings();

    /**
     * sets the player object for this user
     *
     * @param player the new player object
     */
    void setPlayer(Player player);

    /**
     * Causes a new display name to be generated, based on the raw display name, prefix and suffix
     */
    void refreshDisplayName();

    /**
     * sets the raw display name for this user
     *
     * @param displayName the raw display name
     */
    void setDisplayName(String displayName);

    /**
     * @return the prefix for this user
     */
    Component getPrefix();

    /**
     * @return the suffix for this user
     */
    Component getSuffix();

    /**
     * changes the prefix for this user. forces the display name to be regenerated
     *
     * @param prefix the new prefix
     */
    void setPrefix(Component prefix);

    /**
     * changes the suffix for this user. forces the display name to be regenerated
     *
     * @param suffix the new suffix
     */
    void setSuffix(Component suffix);

    /**
     * sets the uuid of this user. shouldn't be used, unless you are gamehandler
     *
     * @param uuid the new uuid
     */
    void setUuid(UUID uuid);

    /**
     * @return Channels a user is listening to
     */
    List<ChatChannel> getChannels();

    /**
     * Add the user to a new channel
     *
     * @param channel string identifier for the channel
     */
    void addListeningChannel(String channel);

    /**
     * Remove the user from a channel
     *
     * @param channel string identifier for the channel
     */
    void removeListeningChannel(String channel);

    /**
     * Get the user's active channel
     *
     * @return ChatChannel the channel the user is speaking to
     */
    ChatChannel getActiveChannel();

    /**
     * Set a user's active channel
     *
     * @param channel string identifier for the channel
     */
    void setActiveChannel(String channel);

    /**
     * Applies the default prefix of the role, if there is one
     */
    void applyRolePrefix();

    /**
     * Applies the default suffix of the role, if there is one
     */
    void applyRoleSuffix();
}
