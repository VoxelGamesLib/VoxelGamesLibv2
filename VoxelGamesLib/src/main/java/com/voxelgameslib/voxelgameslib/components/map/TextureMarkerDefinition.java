package com.voxelgameslib.voxelgameslib.components.map;

import javax.annotation.Nonnull;

public class TextureMarkerDefinition extends BasicMarkerDefinition {

    private String texture;

    public TextureMarkerDefinition(@Nonnull String prefix, @Nonnull String texture) {
        super(prefix);
        this.texture = texture;
    }

    public String getTexture() {
        return this.texture;
    }
}
