package me.minidigger.voxelgameslib.game;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import javax.inject.Singleton;

@Singleton
public class ClassTypeAdapter implements JsonDeserializer<Class>, JsonSerializer<Class> {

  @Override
  public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    try {
      return Class.forName(json.getAsString());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src.getName());
  }
}
