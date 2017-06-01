package me.minidigger.voxelgameslib.game;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.inject.Injector;
import java.lang.reflect.Type;
import java.util.logging.Level;
import javax.inject.Inject;
import jskills.GameRatingInfo;
import lombok.extern.java.Log;

/**
 * Type adapter for the gamemode class
 */
@Log
public class GameModeTypeAdapter implements JsonDeserializer<GameMode> {

  @Inject
  private Injector injector;

  @Override
  public GameMode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    try {
      JsonObject jsonObject = json.getAsJsonObject();

      String name = jsonObject.get("name").getAsString();
      String className = jsonObject.get("className").getAsString();
      GameRatingInfo ratingInfo = context
          .deserialize(jsonObject.get("ratingInfo"), GameRatingInfo.class);

      Class clazz = Class.forName(className);
      //noinspection unchecked
      GameMode gameMode = new GameMode(name, clazz, ratingInfo);
      injector.injectMembers(gameMode);
      return gameMode;
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not deserialize gamemode:\n" + json.toString(), e);
    }
    return null;
  }
}
