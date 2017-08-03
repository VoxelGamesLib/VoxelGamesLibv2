package com.voxelgameslib.voxelgameslib.exception;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.phase.Phase;

/**
 * Thrown when something tries to access a {@link Feature} in a {@link
 * Phase} that is not present at this time.
 */
public class NoSuchFeatureException extends VoxelGameLibException {

    /**
     * @param clazz the class of the feature that was tried to access
     */
    public NoSuchFeatureException(@Nonnull Class<? extends Feature> clazz) {
        super("Could not find feature " + clazz.getSimpleName());
    }
}
