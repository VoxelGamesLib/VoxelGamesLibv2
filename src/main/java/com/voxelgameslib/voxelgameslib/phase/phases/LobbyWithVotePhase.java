package com.voxelgameslib.voxelgameslib.phase.phases;

import com.voxelgameslib.voxelgameslib.feature.features.VoteFeature;

public class LobbyWithVotePhase extends LobbyPhase {

    @Override
    public void init() {
        super.init();

        addFeature(getGame().createFeature(VoteFeature.class, this));
    }
}
