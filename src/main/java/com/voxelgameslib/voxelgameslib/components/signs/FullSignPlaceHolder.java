package com.voxelgameslib.voxelgameslib.components.signs;

import com.voxelgameslib.voxelgameslib.user.User;
import net.kyori.text.Component;
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
