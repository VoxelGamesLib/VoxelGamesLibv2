package me.minidigger.voxelgameslib.command.commands;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;

@Singleton
@Log
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class GameCommands {

  @Inject
  private GameHandler gameHandler;

  public void game(@Nonnull CommandArguments args) {
    // todo game command
  }

  public void gameList(@Nonnull CommandArguments args) {
    Lang.msg(args.getSender(), LangKey.GAME_GAMELIST_HEADER);
    for (Game game : gameHandler.getGames()) {
      Lang.msg(args.getSender(), LangKey.GAME_GAMELIST_ENTRY,
          game.getUuid().toString().split("-")[0], game.getGameMode().getName(),
          game.getActivePhase().getName(), game.getPlayers().size(), game.getSpectators().size());
    }
    Lang.msg(args.getSender(), LangKey.GAME_GAMELIST_FOOTER);
  }

  public void gameListModes(@Nonnull CommandArguments args) {
    StringBuilder sb = new StringBuilder();
    gameHandler.getGameModes().forEach(m -> sb.append(m.getName()).append(", "));
    sb.replace(sb.length() - 2, sb.length(), ".");
    Lang.msg(args.getSender(), LangKey.GAME_GAMEMODE_INSTALLED, sb.toString());
  }

  public void gameStart(@Nonnull CommandArguments args) {
    Optional<GameMode> mode = gameHandler.getGameModes().stream()
        .filter(gameMode -> gameMode.getName().equalsIgnoreCase(args.getArg(0))).findAny();
    if (!mode.isPresent()) {
      Lang.msg(args.getSender(), LangKey.GAME_GAMEMODE_UNKNOWN, args.getArg(0));
      return;
    }
    Game game = gameHandler.startGame(mode.get());

    if (game.getActivePhase().isRunning()) {
      game.join(args.getSender());
      Lang.msg(args.getSender(), LangKey.GAME_GAME_STARTED);
    } else {
      Lang.msg(args.getSender(), LangKey.GAME_COULD_NOT_START);
    }
  }

  public void gameJoin(@Nonnull CommandArguments args) {
    // todo game join command
    Optional<GameMode> mode = gameHandler.getGameModes().stream()
        .filter(gameMode -> gameMode.getName().equalsIgnoreCase(args.getArg(0))).findAny();
    if (!mode.isPresent()) {
      Lang.msg(args.getSender(), LangKey.GAME_GAMEMODE_UNKNOWN, args.getArg(0));
      return;
    }

    Optional<Game> game = gameHandler.findGame(args.getSender(), mode.get());
    if (game.isPresent()) {
      game.get().join(args.getSender());
    } else {
      Lang.msg(args.getSender(), LangKey.GAME_NO_GAME_FOUND);
    }
  }

  public void gameLeave(@Nonnull CommandArguments args) {
    // todo game leave command
  }
}
