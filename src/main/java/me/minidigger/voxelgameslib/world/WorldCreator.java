package me.minidigger.voxelgameslib.world;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.inject.Singleton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.ChatColor;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.ClickEvent;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.ComponentBuilder;
import me.minidigger.voxelgameslib.map.Map;
import me.minidigger.voxelgameslib.map.MapInfo;
import me.minidigger.voxelgameslib.map.MapScanner;
import me.minidigger.voxelgameslib.map.Vector3D;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.utils.ZipUtil;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Handles creation of new worlds/maps
 */
@Log
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class WorldCreator {

  //TODO world creator completer

  @Inject
  private WorldHandler worldHandler;

  @Inject
  private GameHandler gameHandler;

  @Inject
  private MapScanner mapScanner;

  @Inject
  private WorldConfig config;

  @Inject
  private Gson gson;

  private User editor;

  private int step = 0;

  private String worldName;
  private Vector3D center;
  private int radius;
  private String displayName;
  private String author;
  private List<String> gameModes;

  private Map map;

  public void worldcreator(@Nonnull CommandArguments arguments) {
    Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_INFO);
  }

  public void start(@Nonnull CommandArguments arguments) {
    if (editor != null) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_IN_USE,
          editor.getData().getDisplayName());
      return;
    }

    editor = arguments.getSender();
    gameModes = new ArrayList<>();

    arguments.getSender().sendMessage(Lang.trans(LangKey.WORLD_CREATOR_ENTER_WORLD_NAME,
        arguments.getSender().getData().getLocale())
        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/worldcreator world ")).create());

    step = 1;
  }

  public void world(@Nonnull CommandArguments arguments) {
    if (step != 1) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 1);
      return;
    }

    worldName = arguments.getArg(0);

    worldHandler.loadLocalWorld(worldName);
    arguments.getSender().teleport(worldName);

    arguments.getSender().sendMessage(
        Lang.trans(LangKey.WORLD_CREATOR_ENTER_CENTER, arguments.getSender().getData().getLocale())
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldcreator center")).create());

    step = 2;
  }

  public void center(@Nonnull CommandArguments arguments) {
    if (step != 2) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 2);
      return;
    }

    center = arguments.getSender().getLocation();

    arguments.getSender().sendMessage(
        Lang.trans(LangKey.WORLD_CREATOR_ENTER_RADIUS, arguments.getSender().getData().getLocale())
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/worldcreator radius "))
            .create());

    step = 3;
  }

  public void radius(@Nonnull CommandArguments arguments) {
    if (step != 3) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 3);
      return;
    }

    try {
      radius = Integer.parseInt(arguments.getArg(0));
    } catch (NumberFormatException ex) {
      Lang.msg(arguments.getSender(), LangKey.GENERAL_INVALID_NUMBER);
      return;
    }

    arguments.getSender().sendMessage(Lang.trans(LangKey.WORLD_CREATOR_ENTER_DISPLAY_NAME,
        arguments.getSender().getData().getLocale())
        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/worldcreator name ")).create());

    step = 4;
  }

  public void name(@Nonnull CommandArguments arguments) {
    if (step != 4) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 4);
      return;
    }

    StringBuilder sb = new StringBuilder();
    for (String s : arguments.getArgs()) {
      sb.append(s).append(" ");
    }
    displayName = sb.toString().trim();

    arguments.getSender().sendMessage(
        Lang.trans(LangKey.WORLD_CREATOR_ENTER_AUTHOR, arguments.getSender().getData().getLocale(),
            displayName)
            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/worldcreator author "))
            .create());

    step = 5;
  }

  public void author(@Nonnull CommandArguments arguments) {
    if (step != 5) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 5);
      return;
    }

    StringBuilder sb = new StringBuilder();
    for (String s : arguments.getArgs()) {
      sb.append(s).append(" ");
    }
    author = sb.toString().trim();

    Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_AUTHOR_SET, author);
    for (GameMode mode : gameHandler.getGameModes()) {
      arguments.getSender()
          .sendMessage(new ComponentBuilder(mode.getName() + " ").color(ChatColor.YELLOW)
              .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                  "/worldcreator gamemode " + mode.getName())).create());
    }

    arguments.getSender().sendMessage(
        Lang.trans(LangKey.WORLD_CREATOR_DONE_QUERY, arguments.getSender().getData().getLocale())
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldcreator gamemode done"))
            .create());

    step = 6;
  }

  public void gamemode(@Nonnull CommandArguments arguments) {
    if (step != 6) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 6);
      return;
    }

    String gamemode = arguments.getArg(0);
    if (gamemode.equalsIgnoreCase("done")) {
      arguments.getSender().sendMessage(Lang.trans(LangKey.WORLD_CREATOR_EDIT_MODE_ON,
          arguments.getSender().getData().getLocale())
          .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldcreator edit on")).create());
      arguments.getSender().sendMessage(Lang.trans(LangKey.WORLD_CREATOR_EDIT_MODE_OFF,
          arguments.getSender().getData().getLocale())
          .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldcreator edit off")).create());
      step = 7;
    } else {
      gameModes.add(gamemode);
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_GAMEMODE_ADDED);
    }
  }

  public void edit(@Nonnull CommandArguments arguments) {
    if (step != 7) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 7);
      return;
    }

    if (arguments.getArg(0).equalsIgnoreCase("on")) {
      arguments.getSender().executeCommand("editmode on");
    } else if (arguments.getArg(0).equalsIgnoreCase("off")) {
      arguments.getSender().executeCommand("editmode off");
      MapInfo info = new MapInfo(displayName, author, gameModes);
      map = new Map(info, worldName, center, radius);
      map.printSummery(arguments.getSender());
      arguments.getSender().sendMessage(
          Lang.trans(LangKey.WORLD_CREATOR_DONE_QUERY, arguments.getSender().getData().getLocale())
              .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldcreator done")).create());
      step = 8;
    } else {
      Lang.msg(arguments.getSender(), LangKey.GENERAL_INVALID_ARGUMENT, arguments.getArg(0));
    }
  }

  public void done(@Nonnull CommandArguments arguments) {
    if (step != 8) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_WRONG_STEP, step, 8);
      return;
    }

    mapScanner.scan(map);

    File worldFolder = new File(worldHandler.getWorldContainer(), map.getWorldName());

    try {
      FileWriter fileWriter = new FileWriter(new File(worldFolder, "config.json"));
      gson.toJson(map, fileWriter);
      fileWriter.close();
    } catch (IOException e) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_SAVE_CONFIG_ERROR, e.getMessage(),
          e.getClass().getName());
      log.log(Level.WARNING, "Error while saving the world config", e);
      return;
    }

    ZipFile zip;
    try {
      zip = ZipUtil.createZip(worldFolder);
    } catch (ZipException e) {
      Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_SAVE_ZIP_ERROR, e.getMessage(),
          e.getClass().getName());
      log.log(Level.WARNING, "Error while creating the zip", e);
      return;
    }

    try {
      Files.move(zip.getFile(), new File(worldHandler.getWorldsFolder(), zip.getFile().getName()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (!config.maps.contains(map.getInfo())) {
      config.maps.add(map.getInfo());
      worldHandler.saveConfig();
    }

    // TODO debug
//        editor = null;
//        step = 0;
//        worldName = null;
//        center = null;
//        radius = -1;
//        displayName = null;
//        author = null;
//        gameModes = new ArrayList<>();

    Lang.msg(arguments.getSender(), LangKey.WORLD_CREATOR_DONE);
  }
}
