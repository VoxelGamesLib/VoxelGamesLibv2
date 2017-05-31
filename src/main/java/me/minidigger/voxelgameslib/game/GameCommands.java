package me.minidigger.voxelgameslib.game;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.api.command.CommandArguments;
import me.minidigger.voxelgameslib.api.command.CommandExecutor;
import me.minidigger.voxelgameslib.api.command.CommandInfo;
import me.minidigger.voxelgameslib.api.lang.Lang;
import me.minidigger.voxelgameslib.api.lang.LangKey;
import me.minidigger.voxelgameslib.api.role.Role;

@Singleton
@CommandExecutor
@Log
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class GameCommands {

    @Inject
    private GameHandler gameHandler;

    @CommandInfo(name = "game", perm = "command.game", role = Role.DEFAULT)
    public void game(@Nonnull CommandArguments args) {
        // todo game command
    }

    @CommandInfo(name = "game.list", perm = "command.game.list", role = Role.DEFAULT, description = "Shows currently running games")
    public void gameList(@Nonnull CommandArguments args) {
        Lang.msg(args.getSender(), LangKey.GAME_GAMELIST_HEADER);
        for (Game game : gameHandler.getGames()) {
            Lang.msg(args.getSender(), LangKey.GAME_GAMELIST_ENTRY, game.getUuid().toString().split("-")[0], game.getGameMode().getName(), game.getActivePhase().getName(), game.getPlayers().size(), game.getSpectators().size());
        }
        Lang.msg(args.getSender(), LangKey.GAME_GAMELIST_FOOTER);
    }

    @CommandInfo(name = "game.listmodes", perm = "command.game.listmodes", role = Role.DEFAULT, description = "Shows currently installed gamemodes")
    public void gameListModes(@Nonnull CommandArguments args) {
        StringBuilder sb = new StringBuilder();
        gameHandler.getGameModes().forEach(m -> sb.append(m.getName()).append(", "));
        sb.replace(sb.length() - 2, sb.length(), ".");
        Lang.msg(args.getSender(), LangKey.GAME_GAMEMODE_INSTALLED, sb.toString());
    }

    @CommandInfo(name = "game.start", perm = "command.game.start", role = Role.MODERATOR, description = "Starts a new game", min = 1)
    public void gameStart(@Nonnull CommandArguments args) {
        Optional<GameMode> mode = gameHandler.getGameModes().stream().filter(gameMode -> gameMode.getName().equalsIgnoreCase(args.getArg(0))).findAny();
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

    @CommandInfo(name = "game.join", perm = "command.game.join", role = Role.DEFAULT, description = "Joins a game")
    public void gameJoin(@Nonnull CommandArguments args) {
        // todo game join command
        Optional<GameMode> mode = gameHandler.getGameModes().stream().filter(gameMode -> gameMode.getName().equalsIgnoreCase(args.getArg(0))).findAny();
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

    @CommandInfo(name = "game.leave", perm = "command.game.leave", role = Role.DEFAULT, description = "Leave a game")
    public void gameLeave(@Nonnull CommandArguments args) {
        // todo game leave command
    }
}
