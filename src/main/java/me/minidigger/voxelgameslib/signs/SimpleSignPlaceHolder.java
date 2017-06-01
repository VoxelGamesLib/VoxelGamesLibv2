package me.minidigger.voxelgameslib.signs;

import me.minidigger.voxelgameslib.event.events.sign.SignUpdateEvent;

/**
 * A simple sign placeholder that only replaces the key
 */
public interface SimpleSignPlaceHolder extends SignPlaceHolder {

  /**
   * Applies this placeholder to the lines, will only replace the key
   *
   * @param event the event that caused the update
   * @param key the key of this placeholder
   * @return the replacement for the key
   */
  String apply(SignUpdateEvent event, String key);
}
