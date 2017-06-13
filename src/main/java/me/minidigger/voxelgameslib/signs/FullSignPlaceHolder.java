package me.minidigger.voxelgameslib.signs;

import org.bukkit.event.block.SignChangeEvent;

/**
 * Sign placeholder that replaces the whole sign
 */
public interface FullSignPlaceHolder extends SignPlaceHolder {

    /**
     * Applies this placeholder
     *
     * @param event the update event that caused this update
     * @param key   the key of the placeholder
     * @return the replaced sign lines
     */
    String[] apply(SignChangeEvent event, String key);
}
