package me.minidigger.voxelgameslib.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Singleton;

/**
 * Complete for role commands
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class RoleCompleter {

  public List<String> lang(CommandArguments arguments) {
    return CommandUtil.completeWithPlayerNames(arguments.getArg(0));
  }

  public List<String> set(CommandArguments arguments) {
    if (arguments.getNumArgs() == 1) {
      return CommandUtil.completeWithPlayerNames(arguments.getArg(0));
    } else if (arguments.getNumArgs() == 2) {
      return CommandUtil
          .filterTabCompletions(arguments.getNumArgs() == 1 ? "" : arguments.getArg(1),
              Arrays.stream(Role.values()).map(Role::getName).toArray(String[]::new));
    }

    return new ArrayList<>();
  }
}
