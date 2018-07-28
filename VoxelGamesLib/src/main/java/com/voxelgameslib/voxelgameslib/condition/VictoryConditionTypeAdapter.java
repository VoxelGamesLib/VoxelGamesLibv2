package com.voxelgameslib.voxelgameslib.condition;

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
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class VictoryConditionTypeAdapter implements JsonDeserializer<VictoryCondition>, JsonSerializer<VictoryCondition> {

    public static final String DEFAULT_PATH = "com.voxelgameslib.voxelgameslib.condition.conditions";
    private static final Logger log = Logger.getLogger(VictoryConditionTypeAdapter.class.getName());

    @Inject
    private Injector injector;

    @Override
    @Nullable
    public VictoryCondition deserialize(@Nonnull JsonElement json, @Nonnull Type typeOfT, @Nonnull JsonDeserializationContext context)
            throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            // default path
            String name = jsonObject.get("classname").getAsString();
            if (!name.contains(".")) {
                name = DEFAULT_PATH + "." + name;
            }

            Class clazz = Class.forName(name);
            VictoryCondition victoryCondition = context.deserialize(json, clazz);
            injector.injectMembers(victoryCondition);
            return victoryCondition;
        } catch (Exception e) {
            log.log(Level.WARNING, "Could not deserialize victory condition:\n" + json.toString(), e);
        }
        return null;
    }

    @Override
    @Nonnull
    public JsonElement serialize(@Nonnull VictoryCondition src, @Nonnull Type typeOfSrc, @Nonnull JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}