package com.voxelgameslib.voxelgameslib.map;


import com.google.inject.name.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.timings.Timing;

import io.github.classgraph.ClassGraph;

/**
 * Created by Martin on 04.10.2016.
 */
@Singleton
public class MapHandler implements Handler {

    private static final Logger log = Logger.getLogger(MapHandler.class.getName());
    @Inject
    private GameHandler gameHandler;
    @Inject
    @Named("IncludeAddons")
    private ClassGraph scanner;

    //TODO implement chests
    @Nonnull
    private HashMap<String, ChestMarker> chests = new HashMap<>();

    private List<MarkerDefinition> markerDefinitions = new ArrayList<>();

    @Override
    public void enable() {
        try (final Timing timing = new Timing("ScanningFeatures")) {
            scanner.enableClassInfo().enableAnnotationInfo().scan()
                    .getClassesWithAnnotation(FeatureInfo.class.getName()).loadClasses().forEach((clazz) -> {
                if (!Feature.class.isAssignableFrom(clazz)) {
                    log.log(Level.WARNING, "Feature " + clazz.getName() + " is malformed, its not a subtype of feature!");
                    return;
                }
                //noinspection unchecked
                Class<Feature> cls = (Class<Feature>) clazz;
                try {
                    markerDefinitions.addAll(Arrays.asList(cls.newInstance().getMarkers()));
                } catch (InstantiationException | IllegalAccessException e) {
                    log.log(Level.WARNING, "Feature " + cls.getName() + " is malformed!", e);
                }
            });
        }
        log.info("Loaded " + markerDefinitions.size() + " MarkerDefinitions");
    }

    @Override
    public void disable() {

    }

    @Nonnull
    public MarkerDefinition createMarkerDefinition(@Nonnull String markerData) {
        Optional<MarkerDefinition> markerDefinition = markerDefinitions.stream().filter(def -> def.matches(markerData)).findFirst();
        MarkerDefinition def = markerDefinition.orElseGet(() -> new BasicMarkerDefinition(markerData.replace("vgl:", "")));
        def.parse(markerData);
        return def;
    }

    public List<MarkerDefinition> getMarkerDefinitions() {
        return this.markerDefinitions;
    }
}
