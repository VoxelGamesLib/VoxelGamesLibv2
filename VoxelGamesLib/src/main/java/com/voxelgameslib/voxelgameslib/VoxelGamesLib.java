package com.voxelgameslib.voxelgameslib;

import com.google.inject.Injector;

import com.bugsnag.Severity;

import org.inventivetalent.menubuilder.MenuBuilderPlugin;
import org.inventivetalent.menubuilder.inventory.InventoryListener;
import org.objenesis.ObjenesisStd;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.chat.ChatListener;
import com.voxelgameslib.voxelgameslib.command.CommandHandler;
import com.voxelgameslib.voxelgameslib.command.commands.GameCommands;
import com.voxelgameslib.voxelgameslib.command.commands.KitCommands;
import com.voxelgameslib.voxelgameslib.command.commands.LangCommands;
import com.voxelgameslib.voxelgameslib.command.commands.OverrideCommands;
import com.voxelgameslib.voxelgameslib.command.commands.RoleCommands;
import com.voxelgameslib.voxelgameslib.command.commands.StatsCommands;
import com.voxelgameslib.voxelgameslib.command.commands.TestCommands;
import com.voxelgameslib.voxelgameslib.command.commands.TextureCommands;
import com.voxelgameslib.voxelgameslib.command.commands.VGLCommands;
import com.voxelgameslib.voxelgameslib.command.commands.WorldCommands;
import com.voxelgameslib.voxelgameslib.command.commands.WorldRepositoryCommands;
import com.voxelgameslib.voxelgameslib.components.kits.KitHandler;
import com.voxelgameslib.voxelgameslib.components.points.PointHandler;
import com.voxelgameslib.voxelgameslib.components.signs.SignButtons;
import com.voxelgameslib.voxelgameslib.components.signs.SignHandler;
import com.voxelgameslib.voxelgameslib.components.signs.SignListener;
import com.voxelgameslib.voxelgameslib.components.signs.SignPlaceholders;
import com.voxelgameslib.voxelgameslib.components.team.TeamHandler;
import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.editmode.EditMode;
import com.voxelgameslib.voxelgameslib.editmode.WorldCreator;
import com.voxelgameslib.voxelgameslib.editmode.WorldModifyCommands;
import com.voxelgameslib.voxelgameslib.elo.EloHandler;
import com.voxelgameslib.voxelgameslib.error.ErrorHandler;
import com.voxelgameslib.voxelgameslib.event.EventHandler;
import com.voxelgameslib.voxelgameslib.event.events.VoxelGamesLibDisableEvent;
import com.voxelgameslib.voxelgameslib.event.events.VoxelGamesLibEnableEvent;
import com.voxelgameslib.voxelgameslib.exception.LangException;
import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.game.GameListener;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.lang.LangHandler;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.log.LoggingHandler;
import com.voxelgameslib.voxelgameslib.map.MapHandler;
import com.voxelgameslib.voxelgameslib.matchmaking.MatchmakingHandler;
import com.voxelgameslib.voxelgameslib.metrics.MetricHandler;
import com.voxelgameslib.voxelgameslib.module.ModuleHandler;
import com.voxelgameslib.voxelgameslib.persistence.PersistenceHandler;
import com.voxelgameslib.voxelgameslib.role.Role;
import com.voxelgameslib.voxelgameslib.role.RoleHandler;
import com.voxelgameslib.voxelgameslib.startup.StartupHandler;
import com.voxelgameslib.voxelgameslib.startup.StartupListener;
import com.voxelgameslib.voxelgameslib.stats.StatListener;
import com.voxelgameslib.voxelgameslib.stats.StatsHandler;
import com.voxelgameslib.voxelgameslib.stats.Trackable;
import com.voxelgameslib.voxelgameslib.test.TestStuff;
import com.voxelgameslib.voxelgameslib.texture.TextureHandler;
import com.voxelgameslib.voxelgameslib.texture.TextureListener;
import com.voxelgameslib.voxelgameslib.tick.TickHandler;
import com.voxelgameslib.voxelgameslib.timings.Timings;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.user.UserHandler;
import com.voxelgameslib.voxelgameslib.user.UserListener;
import com.voxelgameslib.voxelgameslib.world.WorldHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.CommandReplacements;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.Optional;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import co.aikar.timings.lib.TimingManager;
import me.tom.sparse.spigot.chat.menu.ChatMenuAPI;

public final class VoxelGamesLib extends JavaPlugin {

    private static final Logger log = Logger.getLogger(VoxelGamesLib.class.getName());
    private static TaskChainFactory taskChainFactory;

    private TimingManager timingManager;
    private BukkitCommandManager commandManager;
    private Injector injector;
    private ErrorHandler errorHandler;
    private LoggingHandler loggingHandler;

    @Inject
    private StartupHandler startupHandler;

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
    @Inject
    private PointHandler pointHandler;
    @Inject
    private KitHandler kitHandler;
    @Inject
    private CommandHandler commandHandler;
    @Inject
    private TextureHandler textureHandler;
    @Inject
    private StatsHandler statsHandler;

    @Inject
    private TestStuff testStuff;

    @Override
    public void onLoad() {
        errorHandler = new ErrorHandler(this);
        errorHandler.enable();
    }

    @Override
    public void onEnable() {
        try {
            // logging first, only changes prefixes anyways
            loggingHandler = new LoggingHandler();
            loggingHandler.enable();
            // enable by enabling external stuff. they don't require any VGL stuff

            // timings
            timingManager = TimingManager.of(this);

            // commands
            commandManager = new BukkitCommandManager(this);
            commandManager.setDefaultExceptionHandler((scope, registeredCommand, sender, args, t) -> {
                errorHandler.handle(sender, args, t);
                return false;
            });
            // living on the edge!
            commandManager.enableUnstableAPI("help");

            // task chain
            taskChainFactory = BukkitTaskChainFactory.create(this);
            taskChainFactory.setDefaultErrorHandler((e, t) -> {
                log.severe("Task " + t.hashCode() + " generated an exception:");
                e.printStackTrace();
            });

            // chat menu api
            ChatMenuAPI.init(this);

            // menu builder (excuse the hack, but we want to shade you)
            MenuBuilderPlugin menuBuilderPluginFake = new ObjenesisStd().getInstantiatorOf(MenuBuilderPlugin.class).newInstance();
            menuBuilderPluginFake.inventoryListener = new InventoryListener(menuBuilderPluginFake);
            MenuBuilderPlugin.instance = menuBuilderPluginFake;
            Bukkit.getPluginManager().registerEvents(menuBuilderPluginFake.inventoryListener, this);

            // guice
            VoxelGamesLibModule module = new VoxelGamesLibModule(this, loggingHandler, timingManager,
                commandManager, getVersion(), getDataFolder(), ModuleHandler.getOfferedModules());
            injector = module.createInjector();
            injector.injectMembers(this);

            // startup handler
            startupHandler.registerService("onEnable");
            Bukkit.getPluginManager().registerEvents(injector.getInstance(StartupListener.class), this);

            // then enable all VGL stuff
            Timings.time("EnableAllHandlers", () -> {
                eventHandler.enable();
                configHandler.enable();
                persistenceHandler.enable();
                langHandler.enable();
                tickHandler.enable();
                chatHandler.enable();
                userHandler.enable();
                roleHandler.enable();
                mapHandler.enable();
                worldHandler.enable();
                teamHandler.enable();
                eloHandler.enable();
                matchmakingHandler.enable();
                signHandler.enable();
                metricHandler.enable();
                pointHandler.enable();
                kitHandler.enable();
                commandHandler.enable();
                textureHandler.enable();
                statsHandler.enable();

                gameHandler.enable();
            });

            registerListeners();
        } catch (Exception ex) {
            errorHandler.handle(ex, Severity.ERROR);
        }

        // register commands
        registerCommandContexts();
        registerCommandReplacements();
        registerCommands();
        registerCommandCompletions();

        moduleHandler.enable();

        gameHandler.startDefaultGame();

        getServer().getPluginManager().callEvent(new VoxelGamesLibEnableEvent());

        testStuff.test();

        startupHandler.unregisterService("onEnable");
    }

    @Override
    public void onDisable() {
        try {
            // chat menu api
            ChatMenuAPI.disable();

            getServer().getPluginManager().callEvent(new VoxelGamesLibDisableEvent());
            Timings.time("DisableAllHandlers", () -> {
                configHandler.disable();
                langHandler.disable();
                tickHandler.disable();
                chatHandler.disable();
                userHandler.disable();
                roleHandler.disable();
                mapHandler.disable();
                worldHandler.disable();
                teamHandler.disable();
                eloHandler.disable();
                matchmakingHandler.disable();
                signHandler.disable();
                metricHandler.disable();
                kitHandler.disable();
                pointHandler.disable();
                commandHandler.disable();
                textureHandler.disable();
                statsHandler.disable();

                gameHandler.disable();
                moduleHandler.disable();
                eventHandler.disable();

                persistenceHandler.disable();
                errorHandler.disable();
                loggingHandler.disable();

                injector = null;
            });
        } catch (Exception ex) {
            errorHandler.handle(ex, Severity.ERROR);
        }
    }

    private void registerCommandContexts() {
        CommandContexts<BukkitCommandExecutionContext> con = commandManager.getCommandContexts();
        con.registerIssuerAwareContext(User.class, c -> {
            boolean isOptional = c.hasAnnotation(Optional.class);
            CommandSender sender = c.getSender();
            boolean isPlayerSender = sender instanceof Player;
            if (!c.hasFlag("other")) {
                Player player = isPlayerSender ? (Player) sender : null;
                if (player == null) {
                    if (!isOptional) {
                        throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false);
                    } else {
                        return null;
                    }
                }
                return userHandler.getUser(player.getUniqueId()).orElseThrow(() -> new VoxelGameLibException("Unknown user " + player.getDisplayName()));
            } else {
                String arg = c.popFirstArg();
                if (arg == null) {
                    if (isOptional) {
                        if (c.hasFlag("defaultself")) {
                            if (isPlayerSender) {
                                return userHandler.getUser(((Player) sender).getUniqueId()).orElseThrow(() -> new VoxelGameLibException("Unknown user " + ((Player) sender).getDisplayName()));
                            } else {
                                throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false);
                            }
                        }
                    }
                    return null;
                } else {
                    return userHandler.getUser(arg).orElseThrow(() -> new VoxelGameLibException("Unknown user " + arg));
                }
            }
        });
        con.registerContext(int.class, c -> Integer.parseInt(c.popFirstArg()));
        con.registerContext(GameMode.class, c -> {
            GameMode mode = gameHandler.getGameModes().stream()
                .filter(gameMode -> gameMode.getName().equalsIgnoreCase(c.getFirstArg())).findAny()
                .orElseThrow(() -> new InvalidCommandArgument("Unknown gamemode " + c.getFirstArg()));
            c.popLastArg(); // pop later so that we can get a nice error message
            return mode;
        });
        con.registerContext(Locale.class, c -> {
            Locale locale = Locale.fromName(c.getFirstArg()).orElse(Locale
                .fromTag(c.getFirstArg())
                .orElseThrow(() -> new InvalidCommandArgument("Unknown locale " + c.getFirstArg())));
            c.popFirstArg(); // pop later so that we can get a nice error message
            return locale;
        });
        con.registerContext(Role.class, c -> Role.fromName(c.popFirstArg()));
        con.registerContext(UUID.class, c -> UUID.fromString(c.popFirstArg()));
        con.registerContext(Trackable.class, c -> {
            Trackable s = StatsHandler.fromName(c.getFirstArg())
                .orElseThrow(() -> new InvalidCommandArgument("Unknown stats type " + c.getFirstArg()));
            c.popFirstArg(); // pop later so that we can get a nice error message
            return s;
        });
    }

    private void registerCommandReplacements() {
        CommandReplacements rep = commandManager.getCommandReplacements();
        rep.addReplacement("@gamemodes", gameHandler.getGameModes().stream().map(GameMode::getName).collect(Collectors.joining("|")));
        rep.addReplacement("@locales", Arrays.stream(Locale.values()).map(locale -> locale.getName() + "|" + locale.getTag()).collect(Collectors.joining("|")));
        rep.addReplacement("@roles", Arrays.stream(Role.values()).map(Role::getName).collect(Collectors.joining("|")));
        rep.addReplacement("@stats", statsHandler.getStatTypes().stream().map(stat -> stat.getPrefix() + ":" + stat.name()).collect(Collectors.joining("|")));

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
        comp.registerCompletion("stats", (c) -> statsHandler.getStatTypes().stream().map(stat -> stat.getPrefix() + ":" + stat.name()).collect(Collectors.toList()));
    }

    private void registerCommands() {
        commandManager.registerCommand(injector.getInstance(OverrideCommands.class), true);
        commandManager.registerCommand(injector.getInstance(GameCommands.class));
        commandManager.registerCommand(injector.getInstance(LangCommands.class));
        commandManager.registerCommand(injector.getInstance(VGLCommands.class));
        commandManager.registerCommand(injector.getInstance(RoleCommands.class));
        commandManager.registerCommand(injector.getInstance(WorldCreator.class));
        commandManager.registerCommand(injector.getInstance(WorldModifyCommands.class));
        commandManager.registerCommand(injector.getInstance(WorldCommands.class));
        commandManager.registerCommand(injector.getInstance(EditMode.class));
        commandManager.registerCommand(injector.getInstance(WorldRepositoryCommands.class));
        commandManager.registerCommand(injector.getInstance(LoggingHandler.class));
        commandManager.registerCommand(injector.getInstance(KitCommands.class));
        commandManager.registerCommand(injector.getInstance(TextureCommands.class));
        commandManager.registerCommand(injector.getInstance(StatsCommands.class));
        commandManager.registerCommand(injector.getInstance(TestCommands.class));
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(injector.getInstance(GameListener.class), this);
        pm.registerEvents(injector.getInstance(SignListener.class), this);
        pm.registerEvents(injector.getInstance(UserListener.class), this);
        pm.registerEvents(injector.getInstance(ChatListener.class), this);
        pm.registerEvents(injector.getInstance(SignPlaceholders.class), this);
        pm.registerEvents(injector.getInstance(SignButtons.class), this);
        pm.registerEvents(injector.getInstance(CommandHandler.class), this);
        pm.registerEvents(injector.getInstance(TextureListener.class), this);
        pm.registerEvents(injector.getInstance(StatListener.class), this);
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

    @Nonnull
    public Injector getInjector() {
        return injector;
    }

    public String getVersion() {
        return (VoxelGamesLib.class.getPackage().getImplementationVersion() == null) ? getDescription().getVersion() + "-unknown" : VoxelGamesLib.class.getPackage().getImplementationVersion();
    }
}
