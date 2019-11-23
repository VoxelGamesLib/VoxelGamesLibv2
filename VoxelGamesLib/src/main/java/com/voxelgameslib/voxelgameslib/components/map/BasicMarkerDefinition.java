package com.voxelgameslib.voxelgameslib.components.map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicMarkerDefinition implements MarkerDefinition {

    private String prefix;
    private String data;

    public BasicMarkerDefinition(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean matches(@Nonnull String data) {
        return data.startsWith("vgl:" + prefix);
    }

    @Override
    public void parse(@Nonnull String data) {
        this.data = data.replace("vgl:" + prefix, "");
    }

    @Override
    @Nonnull
    public MarkerDefinition clone() {
        return new BasicMarkerDefinition(prefix);
    }

    @Override
    public boolean isOfSameType(@Nullable MarkerDefinition markerDefinition) {
        return markerDefinition != null && markerDefinition.getPrefix().equals(getPrefix());
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getData() {
        return this.data;
    }
}
