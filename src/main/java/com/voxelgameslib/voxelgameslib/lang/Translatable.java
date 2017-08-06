package com.voxelgameslib.voxelgameslib.lang;

import javax.annotation.Nonnull;

public interface Translatable {
    @Nonnull
    String getDefaultValue();

    @Nonnull
    String[] getArgs();

    @Nonnull
    String name();

    @Nonnull
    Translatable[] getValues();
}
