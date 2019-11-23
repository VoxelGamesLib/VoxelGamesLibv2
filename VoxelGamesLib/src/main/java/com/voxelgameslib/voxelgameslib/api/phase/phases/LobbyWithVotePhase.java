package com.voxelgameslib.voxelgameslib.api.phase.phases;

import com.voxelgameslib.voxelgameslib.api.feature.features.VoteFeature;

public class LobbyWithVotePhase extends LobbyPhase {

    @Override
    public void init() {
        super.init();

        addFeature(getGame().createFeature(VoteFeature.class, this));
    }
}
