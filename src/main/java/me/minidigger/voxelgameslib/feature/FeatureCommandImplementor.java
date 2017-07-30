package me.minidigger.voxelgameslib.feature;

/**
 * A {@link FeatureCommandImplementor} is a piece of logic that applies to a {@link Feature}. It
 * adds the ability to add commands to a {@link Feature}.
 */
public interface FeatureCommandImplementor {

    /**
     * @return an implementation of {@link AbstractFeatureCommand} that houses the commands for a
     * feature
     */
    AbstractFeatureCommand getCommandClass();
}
