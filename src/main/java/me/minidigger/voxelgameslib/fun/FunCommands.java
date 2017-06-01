package me.minidigger.voxelgameslib.fun;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.TextComponent;
import me.minidigger.voxelgameslib.server.Server;

/**
 * Small class to do fun stuff
 */
@Log
@Singleton
public class FunCommands {

  @Inject
  private Gson gson;

  private TacoStuff tacoStuff;

  /**
   * Loads our precious data
   */
  public void load() {
    try {
      tacoStuff = gson.fromJson(
          new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/taco.json"))),
          TacoStuff.class);
      log.info("Tacos loaded!");
    } catch (Exception ex) {
      log.log(Level.WARNING, "Could not load tacos!", ex);
    }
  }

  public void tacoCommand(String recipent) {
    if (tacoStuff == null) {
      arguments.getSender().sendMessage(new TextComponent("No tacos loaded :("));
      return;
    }

    String message = taco(recipent);
    server.broadcastMessage(new TextComponent("The server " + message));
  }

  public List<String> tacoCompleter(CommandArguments arguments) {
    return CommandUtil.completeWithPlayerNames(arguments.getArg(0));
  }

  private String taco(String user) {
    String message = tacoStuff.templates[0];
    message = message.replace("{user}", user);
    message = message.replace("{quality}", getRandom(tacoStuff.parts.quality));
    message = message.replace("{type}", getRandom(tacoStuff.parts.type));
    message = message.replace("{meat}", getRandom(tacoStuff.parts.meat));
    message = message.replaceFirst("\\{topping}", getRandom(tacoStuff.parts.topping));
    message = message.replaceFirst("\\{topping}", getRandom(tacoStuff.parts.topping));
    message = message.replaceFirst("\\{topping}", getRandom(tacoStuff.parts.topping));
    return message;
  }

  private String getRandom(String[] arr) {
    return arr[ThreadLocalRandom.current().nextInt(arr.length)];
  }

  class TacoStuff {

    @Expose
    String[] templates;
    @Expose
    Parts parts;

    class Parts {

      @Expose
      String[] type;
      @Expose
      String[] quality;
      @Expose
      String[] meat;
      @Expose
      String[] topping;
    }
  }
}
