package me.minidigger.voxelgameslib.feature.features;

import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.feature.FeatureInfo;

@FeatureInfo(name = "ClearInventoryFeature", author = "MiniDigger", version = "1.0",
    description = "Simple feature that clears the inventory of all players when the game starts (or a new player joins)")
public class ClearInventoryFeature extends AbstractFeature {

  @Override
  public void start() {
    getPhase().getGame().getPlayers().forEach(user -> user.getPlayer().getInventory().clear());
  }

  @Override
  public void stop() {

  }

  @Override
  public void tick() {

  }

  @Override
  public void init() {

  }

  @Override
  public Class[] getDependencies() {
    return new Class[0];
  }

  @SuppressWarnings("JavaDoc")
  public void onJoin(GameJoinEvent event) {
    if (event.getGame().getUuid().equals(getPhase().getGame().getUuid())) {
      event.getUser().getPlayer().getInventory().clear();
    }
  }
}
