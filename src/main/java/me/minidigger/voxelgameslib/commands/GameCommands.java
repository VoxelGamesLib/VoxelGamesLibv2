package me.minidigger.voxelgameslib.commands;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.extern.java.Log;

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
    public void skipPhase(User sender, @co.aikar.commands.annotation.Optional Integer id) {
        // todo skip phase command
        List<Game> games = gameHandler.getGames(sender.getUuid(), true);
        if (id == null) {
            if (games.size() > 1) {
                //TODO msg
            } else {
                log.finer("skip " + getName());
                games.get(0).endPhase();
            }
        } else {
            if (games.size() > id || id < 0) {
                //TODO msg
            } else {
                log.finer("skip " + getName());
                games.get(id).endPhase();
            }
        }
    }
}
