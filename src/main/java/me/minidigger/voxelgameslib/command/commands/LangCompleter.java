package me.minidigger.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import java.util.Arrays;
import java.util.List;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.lang.Locale;


@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class LangCompleter extends BaseCommand{


  public List<String> lang(CommandArguments arguments) {
    return CommandUtil.completeWithSubCommands(arguments.getArg(0), "lang");
  }

  public List<String> set(CommandArguments arguments) {
    return CommandUtil.filterTabCompletions(arguments.getNumArgs() == 0 ? "" : arguments.getArg(0),
        Arrays.stream(Locale.values()).map(Locale::getTag).toArray(String[]::new));
  }
}
