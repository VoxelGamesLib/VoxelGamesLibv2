package me.minidigger.voxelgameslib;

import co.aikar.commands.ACF;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.CommandManager;
import co.aikar.commands.CommandReplacements;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import co.aikar.timings.lib.TimingManager;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.command.commands.FunCommands;
import me.minidigger.voxelgameslib.command.commands.GameCommands;
import me.minidigger.voxelgameslib.command.commands.LangCommands;
import me.minidigger.voxelgameslib.command.commands.RoleCommands;
import me.minidigger.voxelgameslib.command.commands.VGLCommands;
import me.minidigger.voxelgameslib.command.commands.WorldCommands;
import me.minidigger.voxelgameslib.exception.LangException;
import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.role.Role;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import me.minidigger.voxelgameslib.world.EditMode;
import me.minidigger.voxelgameslib.world.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoxelGamesLib extends JavaPlugin {

  private static TaskChainFactory taskChainFactory;

  private TimingManager timingManager;
  private CommandManager commandManager;
  private Injector injector;

  @Inject
  private UserHandler userHandler;
  @Inject
  private GameHandler gameHandler;

  @Override
  public void onEnable() {
    // Plugin startup logic

    // timings
    timingManager = TimingManager.of(this);

    // commands
    commandManager = ACF.createManager(this);

    // task chain
    taskChainFactory = BukkitTaskChainFactory.create(this);

    // guice
    VoxelGamesLibModule voxelGamesLibModule = new VoxelGamesLibModule(this, timingManager,
        commandManager, getDescription().getVersion(), getDataFolder());
    injector = Guice.createInjector(voxelGamesLibModule);
    injector.injectMembers(this);

    // register commands
    registerCommandContexts();
    registerCommandReplacements();
    registerCommands();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    timingManager = null;
    commandManager = null;
    injector = null;
  }

  private void registerCommandContexts() {
    CommandContexts con = commandManager.getCommandContexts();
    con.registerContext(User.class, c -> userHandler.getUser(c.getSender().getName())
        .orElseThrow(() -> new UserException("Unknown user " + c.getSender().getName())));
    con.registerContext(GameMode.class, c -> gameHandler.getGameModes().stream()
        .filter(gameMode -> gameMode.getName().equalsIgnoreCase(c.getFirstArg())).findAny()
        .orElseThrow(() -> new VoxelGameLibException("Unknown gamemode " + c.getFirstArg())));
    con.registerContext(Locale.class, c -> Locale.fromName(c.getFirstArg()).orElse(Locale
        .fromTag(c.getFirstArg())
        .orElseThrow(() -> new LangException("Unknown locale " + c.getFirstArg()))));
    con.registerContext(Role.class, c -> Role.fromName(c.getFirstArg()));
  }

  private void registerCommandReplacements() {
    CommandReplacements rep = commandManager.getCommandReplacements();
    rep.addReplacement("@gamemodes",
        gameHandler.getGameModes().stream().map(GameMode::getName).collect(
            Collectors.joining("|")));
    rep.addReplacement("@locales",
        Arrays.stream(Locale.values()).map(locale -> locale.getName() + "|" + locale.getTag())
            .collect(Collectors.joining("|")));
    rep.addReplacement("@roles",
        Arrays.stream(Role.values()).map(Role::getName).collect(Collectors.joining("|")));

    rep.addReplacement("%user", "voxelgameslib.role.user");
    rep.addReplacement("%premium", "voxelgameslib.role.premium");
    rep.addReplacement("%moderator", "voxelgameslib.role.moderator");
    rep.addReplacement("%admin", "voxelgameslib.role.admin");
  }

  private void registerCommands() {
    commandManager.registerCommand(injector.getInstance(FunCommands.class));
    commandManager.registerCommand(injector.getInstance(GameCommands.class));
    commandManager.registerCommand(injector.getInstance(LangCommands.class));
    commandManager.registerCommand(injector.getInstance(VGLCommands.class));
    commandManager.registerCommand(injector.getInstance(RoleCommands.class));
    commandManager.registerCommand(injector.getInstance(WorldCreator.class));
    commandManager.registerCommand(injector.getInstance(WorldCommands.class));
    commandManager.registerCommand(injector.getInstance(EditMode.class));
  }


  /**
   * Create a new (normal) chain using the right factory for this server mod
   *
   * @return a normal task chain
   */
  @Nonnull
  public static <T> TaskChain<T> newChain() {
    return taskChainFactory.newChain();
  }

  /**
   * Create a new shared chain using the right factory for this server mod
   *
   * @param name the name of the new shared chain
   * @return a shared task chain
   */
  @Nonnull
  public static <T> TaskChain<T> newSharedChain(@Nonnull String name) {
    return taskChainFactory.newSharedChain(name);
  }

  public Injector getInjector() {
    return injector;
  }
}
