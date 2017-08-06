package com.voxelgameslib.voxelgameslib.game;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.inject.Injector;

import com.voxelgameslib.voxelgameslib.phase.Phase;

import java.lang.reflect.Type;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.java.Log;

/**
 * TypeAdapter for the Game class, redirects gson to the right Game implementation
 */
@Log
@Singleton
public class GameTypeAdapter implements JsonDeserializer<Phase>, JsonSerializer<Phase> {

    @Inject
    private Injector injector;

    @Override
    @Nullable
    public Phase deserialize(@Nonnull JsonElement json, @Nonnull Type typeOfT, @Nonnull JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            // default path
            String name = jsonObject.get("className").getAsString();

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
    @Nonnull
    public JsonElement serialize(@Nonnull Phase src, @Nonnull Type typeOfSrc, @Nonnull JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
