package me.minidigger.voxelgameslib.phase.phases;

import me.minidigger.voxelgameslib.feature.features.VoteFeature;

public class LobbyWithVotePhase extends LobbyPhase {

    @Override
    public void init() {
        super.init();

        addFeature(getGame().createFeature(VoteFeature.class, this));
    }
}
