package com.voxelgameslib.voxelgameslib.error;

import com.bugsnag.Bugsnag;
import com.bugsnag.Severity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.handler.Handler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.CommandIssuer;

public class ErrorHandler implements Handler {

    public static final String BUKKIT_INFO_TAB = "Bukkit Info";
    public static final String WORLD_INFO_TAB = "World Info";
    public static final String EVENT_INFO_TAB = "Event Info";
    public static final String TASK_INFO_TAB = "Task Info";
    public static final String COMMAND_INFO_TAB = "Command Info";
    private static final Logger log = Logger.getLogger(ErrorHandler.class.getName());

    private VoxelGamesLib voxelGamesLib;

    private Bugsnag bugsnag;

    private boolean sendBukkitInfo = true;
    private boolean sendWorldInfo = true;

    private boolean enableBugsnag = System.getProperty("disableBugsnag", "false").equals("false");

    // No guice since we enable on Load
    public ErrorHandler(@Nonnull VoxelGamesLib voxelGamesLib) {
        this.voxelGamesLib = voxelGamesLib;
    }

    @Override
    public void enable() {
        if (enableBugsnag) {
            bugsnag = new Bugsnag("243a3b372720a3695802208b2c46283a", false);
            //TODO configure bugsnag release stage
            bugsnag.setReleaseStage("development");
            bugsnag.setSendThreads(true);
            bugsnag.setAppVersion(voxelGamesLib.getDescription().getVersion());

            LoggedUncaughtExceptionHandler.enable(bugsnag);

            setupCallbacks();

            //injectErrorHandlers(); TODO currently broken
        } else {
            log.warning("Bugsnag is disabled, will not report errors");
        }
    }

    @Override
    public void disable() {
        if (enableBugsnag) {
            LoggedUncaughtExceptionHandler.disable(bugsnag);
        }
    }

    private void setupCallbacks() {
        if (sendBukkitInfo) {
            bugsnag.addCallback(error -> {
                error.addToTab(BUKKIT_INFO_TAB, "Online Players", Bukkit.getOnlinePlayers().size());
                List<String> pluginNames = new ArrayList<>();
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    pluginNames.add(plugin.getName() + " v" + plugin.getDescription().getVersion());
                }
                error.addToTab(BUKKIT_INFO_TAB, "Loaded Plugins", pluginNames);
                error.addToTab(BUKKIT_INFO_TAB, "Version", Bukkit.getVersion());
                error.addToTab(BUKKIT_INFO_TAB, "Spigot: Bungeecord Enabled",
                        Bukkit.spigot().getConfig().getBoolean("settings.bungeecord"));
                error.addToTab(BUKKIT_INFO_TAB, "Spigot: Late Bind Enabled",
                        Bukkit.spigot().getConfig().getBoolean("settings.late-bind"));
                error.addToTab(BUKKIT_INFO_TAB, "Spigot: Netty Threads",
                        Bukkit.spigot().getConfig().getInt("settings.netty-threads"));
            });
        }

        if (sendWorldInfo) {
            bugsnag.addCallback(error -> {
                error.addToTab(WORLD_INFO_TAB, "Worlds Loaded", Bukkit.getWorlds().size());
                Map<String, Integer> entitiesLoaded = new HashMap<>();
                for (World world : Bukkit.getWorlds()) {
                    entitiesLoaded.put(world.getName(), world.getEntities().size());
                }
                error.addToTab(WORLD_INFO_TAB, "Loaded Entities", entitiesLoaded);
                Map<String, Integer> chunksLoaded = new HashMap<>();
                for (World world : Bukkit.getWorlds()) {
                    chunksLoaded.put(world.getName(), world.getLoadedChunks().length);
                }
                error.addToTab(WORLD_INFO_TAB, "Loaded Chunks", chunksLoaded);
            });
        }
    }

    private void injectErrorHandlers() {
        // plugin manager
        try {
            Field pluginManagerField = Bukkit.getServer().getClass().getDeclaredField("pluginManager");
            pluginManagerField.setAccessible(true);
            if (Modifier.isFinal(pluginManagerField.getModifiers())) {
                Field modifierField = Field.class.getDeclaredField("modifiers");
                modifierField.setAccessible(true);
                modifierField.set(pluginManagerField, pluginManagerField.getModifiers() & ~Modifier.FINAL);
            }
            pluginManagerField.set(Bukkit.getServer(), new LoggedPluginManager(voxelGamesLib) {

                private Object timings;
                private final Map<String, Permission> permissions = new HashMap<>();
                private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<>();
                private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<>();
                private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<>();
                private final CommandMap commandMap;

                {
                    Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);
                    commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
                }

                @Override
                protected void customHandler(@Nonnull Throwable e) {
                    if (enableBugsnag) {
                        bugsnag.notify(e.getCause(), Severity.ERROR);
                    }
                    log.info("Caught exception");
                    ACFUtil.sneaky(e); // let bukkit handle it
                }

                @Override
                protected void customHandler(@Nonnull Event event, @Nonnull Throwable e) {
                    if (enableBugsnag) {
                        bugsnag.notify(e.getCause(), Severity.ERROR, (report -> {
                            report.addToTab(EVENT_INFO_TAB, "Event Name", event.getEventName());
                            report.addToTab(EVENT_INFO_TAB, "Is Async", event.isAsynchronous());
                            Map<String, Object> eventData = new HashMap<>();
                            Class eventClass = event.getClass();
                            do {
                                if (eventClass != Event.class) { // Info already provided ^
                                    for (Field field : eventClass.getDeclaredFields()) {
                                        if (field.getType() == HandlerList.class) {
                                            continue; // Unneeded Data
                                        }
                                        field.setAccessible(true);
                                        try {
                                            Object value = field.get(event);
                                            if (value instanceof EntityDamageEvent.DamageModifier) {
                                                value = value.getClass().getCanonicalName() + "."
                                                        + ((EntityDamageEvent.DamageModifier) value).name();
                                            }
                                            if (value instanceof Enum) {
                                                value = value.getClass().getCanonicalName() + "." + ((Enum) value).name();
                                            }
                                            eventData.put(field.getName(), value);
                                        } catch (IllegalAccessException ignored) {
                                        } catch (Throwable internalE) {
                                            eventData.put(field.getName(),
                                                    "Error getting field data: " + internalE.getClass().getCanonicalName() + (
                                                            internalE.getMessage() != null
                                                                    && internalE.getMessage().trim().length() > 0 ? ": " + internalE
                                                                    .getMessage() : ""));
                                        }
                                    }
                                }
                                eventClass = eventClass.getSuperclass();
                            } while (eventClass != null);
                            report.addToTab(EVENT_INFO_TAB, "Event Data", eventData);
                        }));
                    }
                    log.info("Caught exception");
                    ACFUtil.sneaky(e); // let bukkit handle it
                }
            });
        } catch (Throwable e) {
            log.severe("Could not register proxy plugin manager");
            e.printStackTrace();
        }

        // scheduler //TODO figure out how we can better catch those, we dn't want to use nms...
//    try {
//      Field schedulerField = Bukkit.getServer().getClass().getDeclaredField("scheduler");
//      schedulerField.setAccessible(true);
//      if (Modifier.isFinal(schedulerField.getModifiers())) {
//        Field modifierField = Field.class.getDeclaredField("modifiers");
//        modifierField.setAccessible(true);
//        modifierField.set(schedulerField, schedulerField.getModifiers() & ~Modifier.FINAL);
//      }
//      schedulerField.set(Bukkit.getServer(), new LoggedScheduler(voxelGamesLib) {
//        @Override
//        protected void customHandler(int taskID, Throwable e) {
//          if(enableNi)bugsnag.notify(e.getCause(), Severity.ERROR, (report) -> {
//            report.addToTab(TASK_INFO_TAB, "Task ID", taskID);
//          });
//          log.info("Caught exception");
//          ACFUtil.sneaky(e); // let bukkit handle it
//        }
//      });
//    } catch (Throwable e) {
//      log.severe("Could not register proxy scheduler");
//      e.printStackTrace();
//    }
    }

    public void handle(@Nonnull Exception ex, @Nonnull Severity severity, boolean shouldLog) {
        if (enableBugsnag) {
            bugsnag.notify(ex, severity);
        }
        if (shouldLog) {
            log.log(severity.equals(Severity.ERROR) ? Level.SEVERE : Level.WARNING,
                    "Caught exception with level " + severity.getValue(), ex);
        } else {
            log.log(severity.equals(Severity.ERROR) ? Level.SEVERE : Level.WARNING,
                    "Caught exception with level " + severity.getValue());
        }
    }

    public void handle(@Nonnull CommandIssuer sender, @Nonnull List<String> args, @Nonnull Throwable e) {
        if (enableBugsnag) {
            bugsnag.notify(e, Severity.ERROR, (report) -> {
                report.addToTab(COMMAND_INFO_TAB, "sender", ((CommandSender) sender.getIssuer()).getName());
                report.addToTab(COMMAND_INFO_TAB, "args", args.stream().collect(Collectors.joining(" ")));
            });
        }
        log.info("Caught exception");
    }
}
