package com.voxelgameslib.voxelgameslib.phase.phases;

import com.voxelgameslib.voxelgameslib.feature.features.DummyFeature;
import com.voxelgameslib.voxelgameslib.phase.AbstractPhase;

public class DummyPhase extends AbstractPhase {

    @Override
    public void init() {
        setAllowJoin(true);
        setAllowSpectate(false);
        addFeature(getGame().createFeature(DummyFeature.class, this));
    }
}
