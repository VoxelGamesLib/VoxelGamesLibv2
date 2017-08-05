package com.voxelgameslib.voxelgameslib.map;

public interface MarkerDefinition {

    boolean matches(String data);

    void parse(String data);

    MarkerDefinition clone();

    boolean isOfSameType(MarkerDefinition markerDefinition);

    String getPrefix();
}
