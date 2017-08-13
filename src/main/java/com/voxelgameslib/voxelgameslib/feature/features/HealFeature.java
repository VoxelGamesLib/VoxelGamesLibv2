package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.user.User;

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
    public void heal(@Nonnull User user) {
        if (heal) {
            user.getPlayer().setHealth(20.0);
        }
        if (feed) {
            user.getPlayer().setSaturation(20.0f);
        }
    }

    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent event) {
        heal(event.getUser());
    }
}
