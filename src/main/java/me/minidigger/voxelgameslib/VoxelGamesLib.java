package me.minidigger.voxelgameslib;

import com.google.inject.Injector;

import com.bugsnag.Severity;

import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.chat.ChatHandler;
import me.minidigger.voxelgameslib.chat.ChatListener;
import me.minidigger.voxelgameslib.commands.GameCommands;
import me.minidigger.voxelgameslib.commands.LangCommands;
import me.minidigger.voxelgameslib.commands.OverrideCommands;
import me.minidigger.voxelgameslib.commands.RoleCommands;
import me.minidigger.voxelgameslib.commands.VGLCommands;
import me.minidigger.voxelgameslib.commands.WorldCommands;
import me.minidigger.voxelgameslib.commands.WorldRepositoryCommands;
import me.minidigger.voxelgameslib.config.ConfigHandler;
import me.minidigger.voxelgameslib.elo.EloHandler;
import me.minidigger.voxelgameslib.error.ErrorHandler;
import me.minidigger.voxelgameslib.event.EventHandler;
import me.minidigger.voxelgameslib.event.events.VoxelGamesLibDisableEvent;
import me.minidigger.voxelgameslib.exception.LangException;
import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.game.GameListener;
import me.minidigger.voxelgameslib.game.GameMode;
import me.minidigger.voxelgameslib.lang.LangHandler;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.log.LoggingHandler;
import me.minidigger.voxelgameslib.map.MapHandler;
import me.minidigger.voxelgameslib.matchmaking.MatchmakingHandler;
import me.minidigger.voxelgameslib.metrics.MetricHandler;
import me.minidigger.voxelgameslib.module.ModuleHandler;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.role.Role;
import me.minidigger.voxelgameslib.role.RoleHandler;
import me.minidigger.voxelgameslib.components.signs.SignButtons;
import me.minidigger.voxelgameslib.components.signs.SignHandler;
import me.minidigger.voxelgameslib.components.signs.SignListener;
import me.minidigger.voxelgameslib.components.signs.SignPlaceholders;
import me.minidigger.voxelgameslib.team.TeamHandler;
import me.minidigger.voxelgameslib.tick.TickHandler;
import me.minidigger.voxelgameslib.timings.Timings;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import me.minidigger.voxelgameslib.user.UserListener;
import me.minidigger.voxelgameslib.world.EditMode;
import me.minidigger.voxelgameslib.world.WorldCreator;
import me.minidigger.voxelgameslib.world.WorldHandler;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
import lombok.extern.java.Log;

@Log
public final class VoxelGamesLib extends JavaPlugin {

    private static TaskChainFactory taskChainFactory;

    private TimingManager timingManager;
    private BukkitCommandManager commandManager;
    private Injector injector;
    private ErrorHandler errorHandler;
    private LoggingHandler loggingHandler;

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
    @Inject
    private ChatHandler chatHandler;
    @Inject
    private MetricHandler metricHandler;
    @Inject
    private EventHandler eventHandler;


    @Override
    public void onLoad() {
        errorHandler = new ErrorHandler(this);
        errorHandler.start();
    }

    @Override
    public void onEnable() {
        try {
            // logging first, only changes prefixes anyways
            loggingHandler = new LoggingHandler();
            loggingHandler.start();
            // start by enabling external stuff. they don't require any VGL stuff

            // timings
            timingManager = TimingManager.of(this);

            // commands
            commandManager = new BukkitCommandManager(this);
            commandManager.setDefaultExceptionHandler((scope, registeredCommand, sender, args, t) -> {
                errorHandler.handle(sender, args, t);
                return false;
            });

            // task chain
            taskChainFactory = BukkitTaskChainFactory.create(this);
            taskChainFactory.setDefaultErrorHandler((e, t) -> {
                log.severe("Task " + t.hashCode() + " generated an exception:");
                e.printStackTrace();
            });

            // guice
            VoxelGamesLibModule module = new VoxelGamesLibModule(this, loggingHandler, timingManager,
                    commandManager, getDescription().getVersion(), getDataFolder(), ModuleHandler.getOfferedModules());
            injector = module.createInjector();
            injector.injectMembers(this);

            // then enable all VGL stuff
            Timings.time("EnableAllHandler", () -> {
                eventHandler.start();
                configHandler.start();
                persistenceHandler.start();
                langHandler.start();
                tickHandler.start();
                chatHandler.start();
                userHandler.start();
                roleHandler.start();
                mapHandler.start();
                worldHandler.start();
                teamHandler.start();
                eloHandler.start();
                matchmakingHandler.start();
                signHandler.start();
                metricHandler.start();

                gameHandler.start();
                moduleHandler.start();
            });

            // register commands
            registerCommandContexts();
            registerCommandReplacements();
            registerCommands();
            registerCommandCompletions();

            registerListeners();
        } catch (Exception ex) {
            errorHandler.handle(ex, Severity.ERROR);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("stopping");
        try {
            getServer().getPluginManager().callEvent(new VoxelGamesLibDisableEvent());
            Timings.time("DisableAllHandler", () -> {
                configHandler.stop();
                langHandler.stop();
                tickHandler.stop();
                chatHandler.stop();
                userHandler.stop();
                roleHandler.stop();
                mapHandler.stop();
                worldHandler.stop();
                teamHandler.stop();
                eloHandler.stop();
                matchmakingHandler.stop();
                signHandler.stop();
                metricHandler.stop();

                gameHandler.stop();
                moduleHandler.stop();
                eventHandler.stop();

                persistenceHandler.stop();
                errorHandler.stop();
                loggingHandler.stop();

                injector = null;
            });
        } catch (Exception ex) {
            errorHandler.handle(ex, Severity.ERROR);
        }
    }

    private void registerCommandContexts() {
        CommandContexts<BukkitCommandExecutionContext> con = commandManager.getCommandContexts();
        con.registerSenderAwareContext(User.class, c -> {
            if ("false".equalsIgnoreCase(c.getFlagValue("other", "false"))) {
                return userHandler.getUser(c.getSender().getName())
                        .orElseThrow(() -> new UserException("Unknown user " + c.getSender().getName()));
            } else {
                return userHandler.getUser(c.getFirstArg())
                        .orElseThrow(() -> new UserException("Unknown user " + c.getFirstArg()));
            }
        });
        con.registerContext(int.class, c -> Integer.parseInt(c.getFirstArg()));
        con.registerContext(GameMode.class, c -> gameHandler.getGameModes().stream()
                .filter(gameMode -> gameMode.getName().equalsIgnoreCase(c.getFirstArg())).findAny()
                .orElseThrow(() -> new VoxelGameLibException("Unknown gamemode " + c.getFirstArg())));
        con.registerContext(Locale.class, c -> Locale.fromName(c.getFirstArg()).orElse(Locale
                .fromTag(c.getFirstArg())
                .orElseThrow(() -> new LangException("Unknown locale " + c.getFirstArg()))));
        con.registerContext(Role.class, c -> Role.fromName(c.getArgs().get(1)));
    }

    private void registerCommandReplacements() {
        CommandReplacements rep = commandManager.getCommandReplacements();
        rep.addReplacement("@gamemodes", gameHandler.getGameModes().stream().map(GameMode::getName).collect(Collectors.joining("|")));
        rep.addReplacement("@locales", Arrays.stream(Locale.values()).map(locale -> locale.getName() + "|" + locale.getTag()).collect(Collectors.joining("|")));
        rep.addReplacement("@roles", Arrays.stream(Role.values()).map(Role::getName).collect(Collectors.joining("|")));

        rep.addReplacement("%user", "voxelgameslib.role.user");
        rep.addReplacement("%premium", "voxelgameslib.role.premium");
        rep.addReplacement("%moderator", "voxelgameslib.role.moderator");
        rep.addReplacement("%admin", "voxelgameslib.role.admin");
    }

    private void registerCommandCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> comp = commandManager.getCommandCompletions();

        comp.registerCompletion("gamemodes", (c) -> gameHandler.getGameModes().stream().map(GameMode::getName).collect(Collectors.toList()));
        comp.registerCompletion("locales", (c) -> Arrays.stream(Locale.values()).map(locale -> locale.getName() + "|" + locale.getTag()).collect(Collectors.toList()));
        comp.registerCompletion("roles", (c) -> Arrays.stream(Role.values()).map(Role::getName).collect(Collectors.toList()));
    }

    private void registerCommands() {
        commandManager.registerCommand(injector.getInstance(OverrideCommands.class), true);
        commandManager.registerCommand(injector.getInstance(GameCommands.class));
        commandManager.registerCommand(injector.getInstance(LangCommands.class));
        commandManager.registerCommand(injector.getInstance(VGLCommands.class));
        commandManager.registerCommand(injector.getInstance(RoleCommands.class));
        commandManager.registerCommand(injector.getInstance(WorldCreator.class));
        commandManager.registerCommand(injector.getInstance(WorldCommands.class));
        commandManager.registerCommand(injector.getInstance(EditMode.class));
        commandManager.registerCommand(injector.getInstance(WorldRepositoryCommands.class));
        commandManager.registerCommand(injector.getInstance(LoggingHandler.class));
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(injector.getInstance(GameListener.class), this);
        pm.registerEvents(injector.getInstance(SignListener.class), this);
        pm.registerEvents(injector.getInstance(UserListener.class), this);
        pm.registerEvents(injector.getInstance(ChatListener.class), this);
        pm.registerEvents(injector.getInstance(SignPlaceholders.class), this);
        pm.registerEvents(injector.getInstance(SignButtons.class), this);
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
