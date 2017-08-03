package com.voxelgameslib.voxelgameslib.feature;

import co.aikar.commands.BaseCommand;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractFeatureCommand<T extends Feature> extends BaseCommand {

    @Getter
    @Setter
    private T feature;
}
