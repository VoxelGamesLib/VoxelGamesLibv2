package com.voxelgameslib.voxelgameslib.feature.features;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.user.User;

/**
 * Feature that handles dueling.
 */
public class DuelFeature extends AbstractFeature {

    private User one;
    private User two;

    /**
     * @return the first user
     */
    @Nonnull
    public User getOne() {
        return one;
    }

    /**
     * @return the second user
     */
    @Nonnull
    public User getTwo() {
        return two;
    }

    /**
     * @param user the user to not return
     * @return the user that is not user
     */
    @Nonnull
    public User getOther(@Nonnull User user) {
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
    @Nonnull
    public User getOther(@Nonnull UUID uniqueId) {
        if (one.getUuid().equals(uniqueId)) {
            return two;
        } else if (two.getUuid().equals(uniqueId)) {
            return one;
        }
        throw new IllegalArgumentException(uniqueId + " is neither one nor two");
    }

    @Override
    public void enable() {
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
}
