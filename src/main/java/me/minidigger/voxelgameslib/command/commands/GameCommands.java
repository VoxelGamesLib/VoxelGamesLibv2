package me.minidigger.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.user.User;

@Singleton
@Log
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("game")
public class GameCommands extends BaseCommand {

  @Inject
  private GameHandler gameHandler;

  @Subcommand("help")
  @CommandAlias("game")
  @CommandPermission("%user")
  public void game(User sender) {
    // todo game command
  }

  @Subcommand("list")
  @CommandPermission("%user")
  public void gameList(User sender) {
    Lang.msg(sender, LangKey.GAME_GAMELIST_HEADER);
    for (Game game : gameHandler.getGames()) {
      Lang.msg(sender, LangKey.GAME_GAMELIST_ENTRY,
          game.getUuid().toString().split("-")[0], game.getGameMode().getName(),
          game.getActivePhase().getName(), game.getPlayers().size(), game.getSpectators().size());
    }
    Lang.msg(sender, LangKey.GAME_GAMELIST_FOOTER);
  }

  @Subcommand("modes")
  @CommandPermission("%user")
  public void gameListModes(User sender) {
    StringBuilder sb = new StringBuilder();
    gameHandler.getGameModes().forEach(m -> sb.append(m.getName()).append(", "));
    sb.replace(sb.length() - 2, sb.length(), ".");
    Lang.msg(sender, LangKey.GAME_GAMEMODE_INSTALLED, sb.toString());
  }

  @Subcommand("start")
  @CommandCompletion("@gamemodes")
  @Syntax("<mode> - the mode you want to start")
  @CommandPermission("%premium")
  public void gameStart(User sender, GameMode mode) {
    Game game = gameHandler.startGame(mode);

    if (game.getActivePhase().isRunning()) {
      game.join(sender);
      Lang.msg(sender, LangKey.GAME_GAME_STARTED);
    } else {
      Lang.msg(sender, LangKey.GAME_COULD_NOT_START);
    }
  }

  @Subcommand("join")
  @CommandCompletion("@gamemodes")
  @Syntax("<mode> - the mode you want to start")
  @CommandPermission("%user")
  public void gameJoin(User sender, GameMode mode) {
    Optional<Game> game = gameHandler.findGame(sender, mode);
    if (game.isPresent()) {
      game.get().join(sender);
    } else {
      Lang.msg(sender, LangKey.GAME_NO_GAME_FOUND);
    }
  }

  @Subcommand("leave")
  @CommandPermission("%user")
  public void gameLeave(User sender) {
    // todo game leave command
  }

  @Subcommand("skip-phase")
  @CommandPermission("%admin")
  public void skipPhase(User sender) {
    // todo skip phase command
    // need to access the User's game via object

    /*
    if (getGame().isPlaying(arguments.getSender()) || getGame()
        .isSpectating(arguments.getSender())) {
      log.finer("skip " + getName());
      getGame().endPhase();
    }
    */
  }
}
