package me.minidigger.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;
import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.feature.FeatureInfo;
import me.minidigger.voxelgameslib.user.User;
import org.bukkit.event.EventHandler;

@FeatureInfo(name = "HealFeature", author = "MiniDigger", version = "1.0",
    description = "Small feature that heals and feeds players on join")
public class HealFeature extends AbstractFeature {

  @Expose
  private boolean heal = true;
  @Expose
  private boolean feed = true;

  @Override
  public void start() {
    getPhase().getGame().getPlayers().forEach(this::heal);
  }

  /**
   * Heals and feed the user
   *
   * @param user the user
   */
  public void heal(User user) {
    if (heal) {
      user.getPlayer().setHealth(20.0);
    }
    if (feed) {
      user.getPlayer().setSaturation(20.0f);
    }
  }

  @EventHandler
  public void onJoin(GameJoinEvent event) {
    if (event.getGame().getUuid().equals(getPhase().getGame().getUuid())) {
      heal(event.getUser());
    }
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
}
