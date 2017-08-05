package com.voxelgameslib.voxelgameslib.map;

import lombok.Getter;

@Getter
public class BasicMarkerDefinition implements MarkerDefinition {

    private String prefix;
    private String data;

    public BasicMarkerDefinition(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean matches(String data) {
        return data.startsWith("vgl:" + prefix);
    }

    @Override
    public void parse(String data) {
        this.data = data.replace("vgl:" + prefix, "");
    }

    @Override
    public MarkerDefinition clone() {
        return new BasicMarkerDefinition(prefix);
    }

    @Override
    public boolean isOfSameType(MarkerDefinition markerDefinition) {
        return markerDefinition != null && markerDefinition.getPrefix().equals(getPrefix());
    }
}
