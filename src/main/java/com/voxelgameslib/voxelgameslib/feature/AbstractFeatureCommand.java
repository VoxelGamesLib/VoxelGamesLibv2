package com.voxelgameslib.voxelgameslib.feature;

import lombok.Getter;
import lombok.Setter;

import co.aikar.commands.BaseCommand;

public abstract class AbstractFeatureCommand<T extends Feature> extends BaseCommand {

    @Getter
    @Setter
    private T feature;
}
