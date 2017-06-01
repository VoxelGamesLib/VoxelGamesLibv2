package me.minidigger.voxelgameslib.world;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.libs.net.md_5.bungee.api.chat.TextComponent;
import me.minidigger.voxelgameslib.map.Map;

/**
 * Commands related to worlds
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class WorldCommands {

  //TODO world command completer

  @Inject
  private WorldHandler handler;

  public void world(@Nonnull CommandArguments args) {
//TODO remove me once GH-17 is implemented
    args.getSender()
        .sendMessage(new TextComponent("It works! You are on " + args.getSender().getWorld()));
  }

  public void load(@Nonnull CommandArguments args) {
    Optional<Map> o = handler.getMap(args.getArg(0));
    Map map = o.orElseGet(() -> handler.loadMap(args.getArg(0)));

    handler.loadWorld(map);
  }

  public void loadLocal(@Nonnull CommandArguments args) {
    handler.loadLocalWorld(args.getArg(0));
  }

  public void unloadLocal(@Nonnull CommandArguments args) {
    handler.unloadLocalWorld(args.getArg(0));
  }

  public void unload(@Nonnull CommandArguments args) {
    Optional<Map> o = handler.getMap(args.getArg(0));
    if (o.isPresent()) {
      handler.unloadWorld(o.get());
    } else {
      Lang.msg(args.getSender(), LangKey.WORLD_UNKNOWN_MAP, args.getArg(0));
    }
  }

  public void tp(@Nonnull CommandArguments args) {
    Optional<Map> o = handler.getMap(args.getArg(0));
    if (o.isPresent()) {
      Map map = o.get();
      args.getSender().teleport(map.getWorldName(), map.getCenter());
    } else {
      args.getSender().teleport(args.getArg(0));
      Lang.msg(args.getSender(), LangKey.WORLD_UNKNOWN_MAP, args.getArg(0));
    }
  }
}
