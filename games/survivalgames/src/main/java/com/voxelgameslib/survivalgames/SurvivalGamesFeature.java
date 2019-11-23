package com.voxelgameslib.survivalgames;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.api.feature.Feature;
import com.voxelgameslib.voxelgameslib.api.feature.FeatureInfo;

@FeatureInfo(name = "SurvivalGamesFeature", author = "MiniDigger", description = "SurvivalGamesFeature description", version = "1.0.0")
public class SurvivalGamesFeature extends AbstractFeature {

    @Override
    public void enable() {
    }

    @Override
    @Nonnull
    public List<Class<? extends Feature>> getDependencies() {
        return Collections.emptyList();
    }
}
