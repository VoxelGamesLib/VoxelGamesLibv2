package me.minidigger.voxelgameslib.feature.features;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.user.User;

/**
 * Feature that handles dueling.
 */
public class DuelFeature extends AbstractFeature {

  private User one;
  private User two;

  /**
   * @return the first user
   */
  public User getOne() {
    return one;
  }

  /**
   * @return the second user
   */
  public User getTwo() {
    return two;
  }

  /**
   * @param user the user to not return
   * @return the user that is not user
   */
  public User getOther(User user) {
    if (user.equals(one)) {
      return two;
    } else if (user.equals(two)) {
      return one;
    }
    throw new IllegalArgumentException(user.getRawDisplayName() + " is neither one nor two");
  }

  /**
   * @param uniqueId the user to not return
   * @return the user that is not user
   */
  public User getOther(UUID uniqueId) {
    if (one.getUuid().equals(uniqueId)) {
      return two;
    } else if (two.getUuid().equals(uniqueId)) {
      return one;
    }
    throw new IllegalArgumentException(uniqueId + " is neither one nor two");
  }

  @Override
  public void start() {
    if (getPhase().getGame().getPlayers().size() != 2) {
      getPhase().getGame().broadcastMessage(LangKey.DUEL_WRONG_PLAYER_COUNT,
          getPhase().getGame().getPlayers().size());
      getPhase().getGame().abortGame();
      return;
    }

    one = getPhase().getGame().getPlayers().get(0);
    two = getPhase().getGame().getPlayers().get(1);

    if (ThreadLocalRandom.current().nextBoolean()) {
      User temp = one;
      one = two;
      two = temp;
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
