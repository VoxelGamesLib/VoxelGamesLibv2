package com.voxelgameslib.voxelgameslib.api.phase.phases;

import com.voxelgameslib.voxelgameslib.api.feature.features.DummyFeature;
import com.voxelgameslib.voxelgameslib.api.phase.AbstractPhase;

public class DummyPhase extends AbstractPhase {

    @Override
    public void init() {
        setAllowJoin(true);
        setAllowSpectate(false);
        addFeature(getGame().createFeature(DummyFeature.class, this));
    }
}
