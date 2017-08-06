package com.voxelgameslib.voxelgameslib.feature;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.inject.Injector;

import java.lang.reflect.Type;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.java.Log;

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
    @Nullable
    public Feature deserialize(@Nonnull JsonElement json, @Nonnull Type typeOfT, @Nonnull JsonDeserializationContext context)
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
    @Nonnull
    public JsonElement serialize(@Nonnull Feature src, @Nonnull Type typeOfSrc, @Nonnull JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
