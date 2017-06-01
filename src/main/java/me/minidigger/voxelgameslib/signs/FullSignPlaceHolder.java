package me.minidigger.voxelgameslib.signs;

import me.minidigger.voxelgameslib.event.events.sign.SignUpdateEvent;

/**
 * Sign placeholder that replaces the whole sign
 */
public interface FullSignPlaceHolder extends SignPlaceHolder {

  /**
   * Applies this placeholder
   *
   * @param event the update event that caused this update
   * @param key the key of the placeholder
   * @return the replaced sign lines
   */
  String[] apply(SignUpdateEvent event, String key);
}
