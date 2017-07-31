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
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

import org.bukkit.enchantments.Enchantment;

@Singleton
public class EnchantmentTypeAdapter implements JsonDeserializer<Map<Enchantment, Integer>>, JsonSerializer<Map<Enchantment, Integer>> {

    @Override
    public Map<Enchantment, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<Enchantment, Integer> map = new HashMap<>();
        JsonObject obj = json.getAsJsonObject();
        for (Enchantment enchantment : Enchantment.values()) {
            if (obj.get(enchantment.getName()) != null) {
                map.put(enchantment, obj.get(enchantment.getName()).getAsInt());
            }
        }
        return map;
    }

    @Override
    public JsonElement serialize(Map<Enchantment, Integer> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for (Map.Entry<Enchantment, Integer> entry : src.entrySet()) {
            object.add(entry.getKey().getName(), new JsonPrimitive(entry.getValue()));
        }
        return object;
    }
}
