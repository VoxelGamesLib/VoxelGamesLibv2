package com.voxelgameslib.voxelgameslib.map;

import com.voxelgameslib.voxelgameslib.handler.Handler;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import java.util.HashMap;

/**
 * Created by Martin on 04.10.2016.
 */
@Singleton
public class MapHandler implements Handler {

    @Nonnull
    private HashMap<String, ChestMarker> chests = new HashMap<>();

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
