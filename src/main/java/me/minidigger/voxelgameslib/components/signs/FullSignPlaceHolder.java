package me.minidigger.voxelgameslib.components.signs;

import net.kyori.text.Component;

import me.minidigger.voxelgameslib.user.User;

import org.bukkit.Location;

/**
 * Sign placeholder that replaces the whole sign
 */
public interface FullSignPlaceHolder extends SignPlaceHolder {

    /**
     * Applies this placeholder to the lines, can replace every line
     *
     * @param user     the user that will receive the updated sign
     * @param location the location of the sign
     * @param rawLines the plain text content of the lines
     * @param lines    the parsed lines
     * @param key      the key of this placeholder
     * @return the replaced lines
     */
    Component[] apply(User user, Location location, String[] rawLines, Component[] lines, String key);
}
