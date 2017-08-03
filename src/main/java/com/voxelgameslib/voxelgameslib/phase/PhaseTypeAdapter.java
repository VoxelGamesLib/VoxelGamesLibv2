package com.voxelgameslib.voxelgameslib.phase;

import com.google.gson.*;
import com.google.inject.Injector;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.logging.Level;

/**
 * TypeAdapter for the Phase class, redirects gson to the right Phase implementation
 */
@Log
@Singleton
public class PhaseTypeAdapter implements JsonDeserializer<Phase>, JsonSerializer<Phase> {

    public static final String DEFAULT_PATH = "com.voxelgameslib.voxelgameslib.phase.phases";

    @Inject
    private Injector injector;

    @Override
    public Phase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            // default path
            String name = jsonObject.get("className").getAsString();
            if (!name.contains(".")) {
                name = DEFAULT_PATH + "." + name;
            }

            Class clazz = Class.forName(name);
            Phase phase = context.deserialize(json, clazz);
            injector.injectMembers(phase);
            return phase;
        } catch (Exception e) {
            log.log(Level.WARNING, "Could not deserialize phase:\n" + json.toString(), e);
        }
        return null;
    }

    @Override
    public JsonElement serialize(Phase src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
