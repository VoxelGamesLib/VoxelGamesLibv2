package me.minidigger.voxelgameslib;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.CommandReplacements;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import co.aikar.timings.lib.TimingManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.corba.se.impl.activation.CommandHandler;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.command.commands.FunCommands;
import me.minidigger.voxelgameslib.command.commands.GameCommands;
import me.minidigger.voxelgameslib.command.commands.LangCommands;
import me.minidigger.voxelgameslib.command.commands.RoleCommands;
import me.minidigger.voxelgameslib.command.commands.VGLCommands;
import me.minidigger.voxelgameslib.command.commands.WorldCommands;
import me.minidigger.voxelgameslib.config.ConfigHandler;
import me.minidigger.voxelgameslib.elo.EloHandler;
import me.minidigger.voxelgameslib.error.ErrorHandler;
import me.minidigger.voxelgameslib.event.events.VoxelGamesLibDisableEvent;
import me.minidigger.voxelgameslib.exception.LangException;
import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.LangHandler;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.map.MapHandler;
import me.minidigger.voxelgameslib.matchmaking.MatchmakingHandler;
import me.minidigger.voxelgameslib.module.ModuleHandler;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.role.Role;
import me.minidigger.voxelgameslib.role.RoleHandler;
import me.minidigger.voxelgameslib.signs.SignHandler;
import me.minidigger.voxelgameslib.team.TeamHandler;
import me.minidigger.voxelgameslib.tick.TickHandler;
import me.minidigger.voxelgameslib.timings.Timings;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import me.minidigger.voxelgameslib.world.EditMode;
import me.minidigger.voxelgameslib.world.WorldCreator;
import me.minidigger.voxelgameslib.world.WorldHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@Log
public final class VoxelGamesLib extends JavaPlugin {

  private static TaskChainFactory taskChainFactory;

  private TimingManager timingManager;
  private BukkitCommandManager commandManager;
  private Injector injector;

  @Inject
  private ConfigHandler configHandler;
  @Inject
  private TickHandler tickHandler;
  @Inject
  private GameHandler gameHandler;
  @Inject
  private UserHandler userHandler;
  @Inject
  private RoleHandler roleHandler;
  @Inject
  private ErrorHandler errorHandler;
  @Inject
  private MapHandler mapHandler;
  @Inject
  private WorldHandler worldHandler;
  @Inject
  private LangHandler langHandler;
  @Inject
  private ModuleHandler moduleHandler;
  @Inject
  private EloHandler eloHandler;
  @Inject
  private TeamHandler teamHandler;
  @Inject
  private PersistenceHandler persistenceHandler;
  @Inject
  private MatchmakingHandler matchmakingHandler;
  @Inject
  private SignHandler signHandler;

  @Override
  public void onEnable() {
    // start by enabling external stuff. they don't require any VGL stuff

    // timings
    timingManager = TimingManager.of(this);

    // commands
    commandManager = new BukkitCommandManager(this);

    // task chain
    taskChainFactory = BukkitTaskChainFactory.create(this);
    taskChainFactory.setDefaultErrorHandler((e, t) -> {
      log.severe("Task " + t.hashCode() + " generated an exception:");
      e.printStackTrace();
    });

    // guice
    VoxelGamesLibModule voxelGamesLibModule = new VoxelGamesLibModule(this, timingManager,
        commandManager, getDescription().getVersion(), getDataFolder());
    injector = Guice.createInjector(voxelGamesLibModule);
    injector.injectMembers(this);

    // then enable all VGL stuff
    Timings.time("EnableAllHandler", () -> {
      configHandler.start();
      persistenceHandler.start();
      langHandler.start();
      tickHandler.start();
      userHandler.start();
      roleHandler.start();
      errorHandler.start();
      mapHandler.start();
      worldHandler.start();
      teamHandler.start();
      eloHandler.start();
      matchmakingHandler.start();
      signHandler.start();

      gameHandler.start();
      moduleHandler.start();
    });

    // register commands
    registerCommandContexts();
    registerCommandReplacements();
    registerCommands();
    registerCommandCompletions();

    registerListeners();
  }

  @Override
  public void onDisable() {
    getServer().getPluginManager().callEvent(new VoxelGamesLibDisableEvent());
    Timings.time("DisableAllHandler", () -> {
      configHandler.stop();
      langHandler.stop();
      tickHandler.stop();
      userHandler.stop();
      roleHandler.stop();
      errorHandler.stop();
      mapHandler.stop();
      worldHandler.stop();
      teamHandler.stop();
      eloHandler.stop();
      matchmakingHandler.stop();
      signHandler.stop();

      gameHandler.stop();
      moduleHandler.stop();

      persistenceHandler.stop();

      injector = null;
    });
  }

  private void registerCommandContexts() {
    CommandContexts<BukkitCommandExecutionContext> con = commandManager.getCommandContexts();
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

  private void registerCommandCompletions() {
    CommandCompletions<CommandSender, BukkitCommandCompletionContext> comp = commandManager
        .getCommandCompletions();

    //TODO add tab completion everywhere
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

  private void registerListeners() {
    //TODO register event listeners
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
