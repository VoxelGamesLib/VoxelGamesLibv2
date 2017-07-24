package me.minidigger.voxelgameslib.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;

import lombok.extern.java.Log;

@Log
@Singleton
public class EventHandler implements Handler, Listener{

    private static final EventFilter filterPlayers = (event, registeredListener, user) ->
            user.filter(user1 -> registeredListener.getGame().isPlaying(user1.getUuid())).isPresent();

    private final EventExecutor eventExecutor = (listener, event) -> callEvent(event);

    private Map<Class<? extends Event>, List<RegisteredListener>> activeEvents = new HashMap<>();
    private Map<UUID, List<RegisteredListener>> activeListeners = new HashMap<>();

    private Map<Class<? extends Event>, Method> reflectionCachePlayer = new HashMap<>();
    private Map<Class<? extends Event>, Method> reflectionCacheUser = new HashMap<>();

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private UserHandler userHandler;

    public void registerEvents(Listener listener, Game game) {
        Set<Class<Event>> newEvents = new HashSet<>();
        Arrays.stream(listener.getClass().getMethods()).filter((method -> method.isAnnotationPresent(GameEvent.class))).forEach(
                method -> {
                    if (method.getParameterCount() != 1) {
                        log.warning("Invalid parameters for " + listener.getClass().getName() + " " + method.toString());
                        return;
                    }

                    if (Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        //noinspection unchecked
                        Class<Event> eventClass = (Class<Event>) method.getParameterTypes()[0];
                        GameEvent annotation = method.getAnnotation(GameEvent.class);

                        RegisteredListener registeredListener = new RegisteredListener(listener, game, eventClass, method, new ArrayList<>());

                        if (annotation.filterPlayers()) {
                            registeredListener.addFilter(filterPlayers);
                        }

                        activeListeners.computeIfAbsent(game.getUuid(), (key) -> new ArrayList<>()).add(registeredListener);

                        activeEvents.computeIfAbsent(eventClass, (key) -> {
                            newEvents.add(eventClass);
                            return new ArrayList<>();
                        }).add(registeredListener);
                    } else {
                        log.warning("Invalid parameter for " + listener.getClass().getName() + " " + method.toString());
                        return;
                    }
                }
        );

        // check if we need to register a new event
        newEvents.forEach(eventClass -> {
            System.out.println("add new event " + eventClass.getName());
            if (eventClass.getName().contains("GameJoinEvent")) {
                System.out.println("REGISTER JOIN");
                Bukkit.getServer().getPluginManager().registerEvent(eventClass, this, EventPriority.HIGH, eventExecutor, voxelGamesLib);
            } else {
                Bukkit.getServer().getPluginManager().registerEvent(eventClass, this, EventPriority.NORMAL, eventExecutor, voxelGamesLib);
            }
        });

        // register normal events
        //Bukkit.getServer().getPluginManager().registerEvents(listener, voxelGamesLib);
    }

    public void unregister(Listener listener, Game game) {
        //noinspection unchecked
        Arrays.stream(listener.getClass().getMethods())
                .filter((method -> method.isAnnotationPresent(GameEvent.class)))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                .map(method -> (Class<Event>) method.getParameterTypes()[0]).forEach(
                eventClass -> activeEvents.get(eventClass).removeIf(registeredListener -> registeredListener.getListener().equals(listener)));

        if (activeListeners.containsKey(game.getUuid())) {
            activeListeners.get(game.getUuid()).removeIf(registeredListener -> registeredListener.getListener().equals(listener));
            if (activeListeners.get(game.getUuid()).size() == 0) {
                activeListeners.remove(game.getUuid());
            }
        }

        HandlerList.unregisterAll(listener);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public <T extends Event> void callEvent(T event) {
        if (event.getEventName().equalsIgnoreCase("GameJoinEvent")) {
            System.out.println("CALL JOIN");
            new RuntimeException().printStackTrace();
        }
        if (activeEvents.containsKey(event.getClass())) {
            activeEvents.get(event.getClass()).forEach(registeredListener -> {
                Optional<User> user = Optional.empty();
                boolean tried = false;
                for (EventFilter filter : registeredListener.getFilters()) {
                    if (!user.isPresent() && !tried) {
                        user = figureOutUser(event);
                        tried = true;
                    }
                    if (!filter.filter(event, registeredListener, user)) {
                        return;
                    }
                }

                try {
                    registeredListener.getMethod().invoke(registeredListener.getListener(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.log(Level.SEVERE, "Error while calling eventhandler!", e);
                }
            });
        }
    }

    private <T extends Event> Optional<User> figureOutUser(T event) {
        if (event instanceof PlayerEvent) {
            return userHandler.getUser(((PlayerEvent) event).getPlayer().getUniqueId());
        } else if (event instanceof me.minidigger.voxelgameslib.event.events.player.PlayerEvent) {
            return Optional.of(((me.minidigger.voxelgameslib.event.events.player.PlayerEvent) event).getUser());
        }

        // search for method to get player
        if (!reflectionCachePlayer.containsKey(event.getClass()) && !reflectionCacheUser.containsKey(event.getClass())) {
            for (Method m : event.getClass().getMethods()) {
                if (m.getReturnType().equals(User.class)) {
                    reflectionCacheUser.put(event.getClass(), m);
                    break;
                } else if (m.getReturnType().equals(Player.class)) {
                    reflectionCachePlayer.put(event.getClass(), m);
                    break;
                }
            }
        }

        // check cache to find user
        if (reflectionCacheUser.containsKey(event.getClass())) {
            Method method = reflectionCacheUser.get(event.getClass());
            try {
                return Optional.of((User) method.invoke(event));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        } else if (reflectionCacheUser.containsKey(event.getClass())) {
            Method method = reflectionCachePlayer.get(event.getClass());
            try {
                return userHandler.getUser(((Player) method.invoke(event)).getUniqueId());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
