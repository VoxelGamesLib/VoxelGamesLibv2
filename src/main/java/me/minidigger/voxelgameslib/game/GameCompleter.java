package me.minidigger.voxelgameslib.game;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.java.Log;

@Log
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class GameCompleter {

  @Inject
  private GameHandler gameHandler;

  public List<String> game(@Nonnull CommandArguments args) {
    return CommandUtil.completeWithSubCommands(args.getArg(0), "game");
  }

  public List<String> list(@Nonnull CommandArguments args) {
    return new ArrayList<>();
  }

  public List<String> listmodes(@Nonnull CommandArguments args) {
    return new ArrayList<>();
  }

  public List<String> start(@Nonnull CommandArguments args) {
    return CommandUtil.filterTabCompletions(args.getArg(0), gameHandler.getGameModes());
  }

  public List<String> join(@Nonnull CommandArguments args) {
    return CommandUtil.filterTabCompletions(args.getArg(0), gameHandler.getGameModes());
  }

  public List<String> leave(@Nonnull CommandArguments args) {
    return new ArrayList<>(); //TODO game.leave completer
  }
}
