package com.voxelgameslib.voxelgameslib.map;

import javax.annotation.Nonnull;

import lombok.Getter;

@Getter
public class TextureMarkerDefinition extends BasicMarkerDefinition {

    private String texture;

    public TextureMarkerDefinition(@Nonnull String prefix, @Nonnull String texture) {
        super(prefix);
        this.texture = texture;
    }
}
