package com.voxelgameslib.voxelgameslib.feature;

import com.google.gson.*;
import com.google.inject.Injector;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.logging.Level;

/**
 * TypeAdapter for the Feature class, redirects gson to the right Feature implementation
 */
@Log
@Singleton
public class FeatureTypeAdapter implements JsonDeserializer<Feature>, JsonSerializer<Feature> {

    public static final String DEFAULT_PATH = "com.voxelgameslib.voxelgameslib.feature.features";

    @Inject
    private Injector injector;

    @Override
    public Feature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            // default path
            String name = jsonObject.get("name").getAsString();
            if (!name.contains(".")) {
                name = DEFAULT_PATH + "." + name;
            }

            Class clazz = Class.forName(name);
            Feature feature = context.deserialize(json, clazz);
            injector.injectMembers(feature);
            return feature;
        } catch (Exception e) {
            log.log(Level.WARNING, "Could not deserialize feature:\n" + json.toString(), e);
        }
        return null;
    }

    @Override
    public JsonElement serialize(Feature src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
