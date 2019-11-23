package com.voxelgameslib.voxelgameslib.components.map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MarkerDefinition {

    boolean matches(@Nonnull String data);

    void parse(@Nonnull String data);

    @Nonnull
    MarkerDefinition clone();

    boolean isOfSameType(@Nullable MarkerDefinition markerDefinition);

    @Nonnull
    String getPrefix();
}
