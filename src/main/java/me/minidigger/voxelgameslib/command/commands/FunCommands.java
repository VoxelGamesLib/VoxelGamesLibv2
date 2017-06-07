package me.minidigger.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.user.User;
import net.kyori.text.TextComponent;

/**
 * Small class to do fun stuff
 */
@Log
@Singleton
@CommandAlias("fun")
public class FunCommands extends BaseCommand {

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

  @Subcommand("taco")
  @CommandAlias("taco")
  @Syntax("<recipent> - whoever should receive the taco")
  @CommandCompletion("@players")
  @CommandPermission("%user")
  public void tacoCommand(User user, String recipent) {
    if (tacoStuff == null) {
      user.sendMessage(new TextComponent("No tacos loaded :("));
      return;
    }

    String message = taco(recipent);
    Lang.broadcast(new TextComponent("The server " + message));
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
