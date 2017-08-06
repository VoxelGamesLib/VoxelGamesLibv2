package com.voxelgameslib.voxelgameslib.map;

import lombok.Getter;

@Getter
public class TextureMarkerDefinition extends BasicMarkerDefinition {

    private String texture;

    public TextureMarkerDefinition(String prefix, String texture) {
        super(prefix);
        this.texture = texture;
    }
}
