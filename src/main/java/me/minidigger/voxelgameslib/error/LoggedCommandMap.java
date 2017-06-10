package me.minidigger.voxelgameslib.error;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

/***
 * @author Jackson
 * @version 1.0
 */
public abstract class LoggedCommandMap extends SimpleCommandMap {

  private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);

  private SimpleCommandMap delegate;
  @SuppressWarnings("unused")
  private Map<String, Command> knownCommands;

  private boolean doRegister = false;

  public LoggedCommandMap(SimpleCommandMap bukkitCommandMap) {
    super(Bukkit.getServer());
    doRegister = true;
    this.delegate = bukkitCommandMap;
    //noinspection Since15
    try {
      Field knownCommandsField = bukkitCommandMap.getClass().getDeclaredField("knownCommands");
      knownCommandsField.setAccessible(true);
      //noinspection unchecked
      this.knownCommands = (Map<String, Command>) knownCommandsField.get(this.delegate);
      this.setKnownCommands(this.knownCommands);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void setKnownCommands(Map<String, Command> knownCommands);

  protected abstract void customHandler(Command command, String commandString, Throwable e);

  @Override
  public void registerAll(String s, List<Command> list) {
    if (doRegister) {
      delegate.registerAll(s, list);
    }
  }

  @Override
  public boolean register(String s, String s1, Command command) {
    return !doRegister || delegate.register(s, s1, command);
  }

  @Override
  public boolean register(String s, Command command) {
    return !doRegister || delegate.register(s, command);
  }

  @Override
  public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
    String[] args = PATTERN_ON_SPACE.split(commandLine);
    if (args.length == 0) {
      return false;
    } else {
      String sentCommandLabel = args[0].toLowerCase();
      Command target = this.getCommand(sentCommandLabel);
      boolean doTimings = false;
      try {
        target.getClass().getField("timings");
        doTimings = true;
      } catch (Throwable e) {
      }
      if (target == null) {
        return false;
      } else {
        try {
          if (doTimings) {
            target.timings.startTiming();
          }
          target.execute(sender, sentCommandLabel,
              Arrays.copyOfRange(args, 1, args.length));
          if (doTimings) {
            target.timings.stopTiming();
          }
          return true;
        } catch (Throwable var8) {
          if (doTimings) {
            target.timings.stopTiming();
          }
          customHandler(target, commandLine, new CommandException(
              "Unhandled exception executing \'" + commandLine + "\' in " + target, var8));
          return true;
        }
      }
    }
  }

  @Override
  public void clearCommands() {
    if (doRegister) {
      delegate.clearCommands();
    }
  }

  @Override
  public Command getCommand(String s) {
    return delegate.getCommand(s);
  }

  @Override
  public List<String> tabComplete(CommandSender commandSender, String s)
      throws IllegalArgumentException {
    return delegate.tabComplete(commandSender, s);
  }
}