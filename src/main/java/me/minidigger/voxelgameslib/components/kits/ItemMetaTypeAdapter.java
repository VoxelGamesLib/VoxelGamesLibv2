package me.minidigger.voxelgameslib.components.kits;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.logging.Level;
import javax.inject.Singleton;

import org.bukkit.inventory.meta.ItemMeta;

import lombok.extern.java.Log;

@Log
@Singleton
public class ItemMetaTypeAdapter implements JsonDeserializer<ItemMeta>, JsonSerializer<ItemMeta> {
    @Override
    public ItemMeta deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            // default path
            String name = jsonObject.get("className").getAsString();

            Class clazz = Class.forName(name);
            return context.deserialize(json, clazz);
        } catch (Exception e) {
            log.log(Level.WARNING, "Could not deserialize itemmeta:\n" + json.toString(), e);
        }
        return null;
    }

    @Override
    public JsonElement serialize(ItemMeta src, Type typeOfSrc, JsonSerializationContext context) {
        System.out.println("serialize meta");
        JsonObject object = (JsonObject) context.serialize(src);
        object.add("className", new JsonPrimitive(src.getClass().getName()));
        return object;
    }
}
