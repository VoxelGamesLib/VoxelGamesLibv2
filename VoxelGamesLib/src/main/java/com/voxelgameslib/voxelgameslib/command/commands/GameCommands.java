package com.voxelgameslib.voxelgameslib.command.commands;

import net.kyori.text.TextComponent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("game")
public class GameCommands extends BaseCommand {

    private static final Logger log = Logger.getLogger(GameCommands.class.getName());
    @Inject
    private GameHandler gameHandler;
    @Inject
    private GlobalConfig config;

    @HelpCommand
    @CommandPermission("%user")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("list")
    @CommandPermission("%user")
    public void gameList(@Nonnull User sender) {
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
    public void gameListModes(@Nonnull User sender) {
        StringBuilder sb = new StringBuilder();
        gameHandler.getGameModes().forEach(m -> sb.append(m.getName()).append(", "));
        sb.replace(sb.length() - 2, sb.length(), ".");
        Lang.msg(sender, LangKey.GAME_GAMEMODE_INSTALLED, sb.toString());
    }

    @Subcommand("start")
    @CommandCompletion("@gamemodes")
    @Syntax("<mode> - the mode you want to start")
    @CommandPermission("%premium")
    public void gameStart(@Nonnull User sender, @Nonnull GameMode mode) {
        if (handleGameLeaving(sender, false)) return;

        Game game = gameHandler.startGame(mode);

        if (game.getActivePhase().isRunning()) {
            game.join(sender);
            Lang.msg(sender, LangKey.GAME_GAME_STARTED);
            if (config.announceNewGame) {
                //TODO figure out which command to enter
                Lang.broadcast(LangKey.GAME_ANNOUNCE_GAME_STARTED, "/game joinuuid " + game.getUuid().toString(), sender.getDisplayName(), mode.getName());
            }
        } else {
            Lang.msg(sender, LangKey.GAME_COULD_NOT_START);
        }
    }

    @Subcommand("stop")
    @CommandPermission("%admin")
    public void gameStop(@Nonnull User sender, @Nullable @co.aikar.commands.annotation.Optional String gameId) {
        List<Game> games = gameHandler.getGames(sender.getUuid(), false);
        if (games.size() == 0) {
            Lang.msg(sender, LangKey.GAME_STOP_IN_NO_GAME);
            games = gameHandler.getGames(sender.getUuid(), true);

            if (games.size() == 0) {
                Lang.msg(sender, LangKey.GAME_STOP_IN_NO_GAME_SPEC);
            }
        } else if (games.size() == 1) {
            games.get(0).abortGame();
            return;
        }

        if (gameId == null) {
            games.forEach(Game::abortGame);
        } else {
            // todo we need a better way to have game identifiers
            games.get(Integer.parseInt(gameId)).abortGame();
        }
    }

    @Subcommand("join")
    @CommandCompletion("@gamemodes")
    @Syntax("<mode> - the mode you want to enable")
    @CommandPermission("%user")
    public void gameJoin(@Nonnull User sender, @Nonnull GameMode mode) {
        if (handleGameLeaving(sender, false)) return;
        Optional<Game> game = gameHandler.findGame(sender, mode);
        if (game.isPresent()) {
            game.get().join(sender);
        } else {
            Lang.msg(sender, LangKey.GAME_NO_GAME_TO_JOIN_FOUND);
        }
    }

    @Subcommand("joinuuid")
    @CommandCompletion("@gamemodes")
    @Syntax("<uuid> - the uuid of the game you want to join")
    @CommandPermission("%user")
    public void gameJoinUUID(@Nonnull User sender, @Nonnull UUID id) {
        if (handleGameLeaving(sender, false)) return;
        Optional<Game> game = gameHandler.getGames().stream().filter(g -> g.getUuid().equals(id)).findAny();
        if (game.isPresent()) {
            game.get().join(sender);
        } else {
            Lang.msg(sender, LangKey.GAME_COULD_NOT_FIND_GAME, id);
        }
    }

    @Subcommand("leave")
    @CommandPermission("%user")
    public void gameLeave(@Nonnull User sender) {
        List<Game> games = gameHandler.getGames(sender.getUuid(), true);

        if (games.size() == 0) {
            Lang.msg(sender, LangKey.GAME_NOT_FOUND);
            return;
        } else if (games.size() > 1) {
            Lang.msg(sender, LangKey.GAME_IN_MORE_THAN_ONE_GAME);
        }

        // leave everything but the default game
        games.stream()
                .filter(game -> !game.equals(gameHandler.getDefaultGame()))
                .forEach(game -> game.leave(sender, gameHandler.getDefaultGame() == null));
    }

    @Subcommand("skip-phase")
    @CommandPermission("%admin")
    public void skipPhase(@Nonnull User sender, @Nullable @co.aikar.commands.annotation.Optional Integer id) {
        List<Game> games = gameHandler.getGames(sender.getUuid(), true);
        if (id == null) {
            if (games.size() > 1) {
                Lang.msg(sender, LangKey.GAME_IN_TOO_MANY_GAMES);
            } else {
                log.finer("skip " + games.get(0).getActivePhase().getName());
                games.get(0).endPhase();
            }
        } else {
            if (games.size() > id || id < 0) {
                Lang.msg(sender, LangKey.GAME_INVALID_GAME_ID);
            } else {
                log.finer("skip " + games.get(id).getActivePhase().getName());
                games.get(id).endPhase();
            }
        }
    }

    @Subcommand("shout")
    @CommandAlias("shout|s")
    public void shout(@Nonnull User sender, @Nonnull String message) {
        List<Game> games = gameHandler.getGames(sender.getUuid(), true);

        if (games.size() == 0) {
            Lang.msg(sender, LangKey.GAME_NOT_IN_GAME_NO_ID);
            return;
        } else if (games.size() > 1) {
            Lang.msg(sender, LangKey.GAME_IN_TOO_MANY_GAMES);
            return;
        } else {
            games.get(0).broadcastMessage(TextComponent.of(message));
        }
    }

    private boolean handleGameLeaving(User sender, boolean shouldTeleportToSpawn) {
        List<Game> games = gameHandler.getGames(sender.getUuid(), true);
        if (games.size() != 0) {
            if (games.size() == 1 && games.get(0).getGameMode().equals(gameHandler.getDefaultGame().getGameMode())) {
                // leave the default game
                games.get(0).leave(sender, shouldTeleportToSpawn);
            } else {
                Lang.msg(sender, LangKey.GAME_YOU_CANNOT_BE_IN_MULTIPLE_GAMES);
                return true;
            }
        }
        return false;
    }
}
