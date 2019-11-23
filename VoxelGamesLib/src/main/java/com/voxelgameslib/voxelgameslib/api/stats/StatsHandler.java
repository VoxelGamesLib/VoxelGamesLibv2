package com.voxelgameslib.voxelgameslib.api.stats;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import com.bugsnag.Severity;

import net.kyori.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.components.user.UserHandler;
import com.voxelgameslib.voxelgameslib.internal.error.ErrorHandler;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;
import com.voxelgameslib.voxelgameslib.internal.persistence.PersistenceHandler;
import com.voxelgameslib.voxelgameslib.internal.timings.Timing;
import com.voxelgameslib.voxelgameslib.util.Pair;

import org.bukkit.Bukkit;

import io.github.classgraph.ClassGraph;

@Singleton
public class StatsHandler implements Handler {

    private static final Logger log = Logger.getLogger(StatsHandler.class.getName());

    @Inject
    private VoxelGamesLib vgl;
    @Inject
    private Injector injector;
    @Inject
    private UserHandler userHandler;
    @Inject
    private PersistenceHandler persistenceHandler;
    @Inject
    @Named("IncludeAddons")
    private ClassGraph scanner;
    @Inject
    private ErrorHandler errorHandler;

    private List<Stat> statTypes = new ArrayList<>();
    private static List<Trackable> trackables = new ArrayList<>();

    @Override
    public void enable() {
        registerTrackable(StatType.JOIN_COUNT);

        Bukkit.getScheduler().runTaskTimer(vgl, () -> statTypes.stream().filter(Stat::shouldTick).forEach(Stat::tickOneMinute), 20 * 60, 20 * 60);

        try (final Timing timing = new Timing("RegisterStatTypes")) {
            //noinspection unchecked
            scanner.enableClassInfo().scan().getSubclasses(Stat.class.getName()).loadClasses()
                    .forEach(clazz -> registerStatType((Class<? extends Stat>) clazz));
        }
        log.info("Registered " + statTypes.size() + " StatsTypes");

        Bukkit.getScheduler().runTaskTimer(vgl, () -> userHandler.getUsers().forEach(user -> {
            if (user.getUserData().getStats().values().stream().anyMatch(StatInstance::isDirty)) {
                log.finer("Persisting stats for " + user.getRawDisplayName());
                persistenceHandler.getProvider().saveUser(user.getUserData());
            }
        }), 60 * 20, 60 * 20);
    }

    @Override
    public void disable() {

    }

    private void registerStatType(Class<? extends Stat> clazz) {
        Stat stat = injector.getInstance(clazz);
        stat.getType().setStat(stat);
        statTypes.add(stat);
        if (stat.getListener() != null) {
            Bukkit.getPluginManager().registerEvents(stat.getListener(), vgl);
        }
    }

    public void registerTrackable(Trackable trackable) {
        trackables.add(trackable);
    }

    public List<Trackable> getStatTypes() {
        return trackables.stream().flatMap(trackable -> Arrays.stream(trackable.getValues())).collect(Collectors.toList());
    }

    public static Optional<Trackable> fromName(String name) {
        if (!name.contains(":")) {
            name = "VGL:" + name;
        }
        final String fName = name;
        return trackables.stream()
                .flatMap(trackable -> Arrays.stream(trackable.getValues()))
                .filter(trackable -> (trackable.getPrefix().toUpperCase() + ":" + trackable.name().toUpperCase()).equals(fName.toUpperCase()))
                .findAny();
    }

    private LoadingCache<Pair<Trackable, Integer>, List<Pair<UUID, Double>>> uuidCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<Pair<Trackable, Integer>, List<Pair<UUID, Double>>>() {
                @Override
                public List<Pair<UUID, Double>> load(Pair<Trackable, Integer> key) throws Exception {
                    return persistenceHandler.getProvider().getTopWithUUID(key.getFirst(), key.getSecond());
                }
            });

    public List<Pair<UUID, Double>> getTopWithUUID(Trackable type, int amount) {
        try {
            return uuidCache.get(new Pair<>(type, amount));
        } catch (ExecutionException e) {
            errorHandler.handle(e, Severity.WARNING, true);
        }

        return new ArrayList<>();
    }

    private LoadingCache<Pair<Trackable, Integer>, List<Pair<Component, Double>>> nameCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<Pair<Trackable, Integer>, List<Pair<Component, Double>>>() {
                @Override
                public List<Pair<Component, Double>> load(Pair<Trackable, Integer> key) throws Exception {
                    return persistenceHandler.getProvider().getTopWithName(key.getFirst(), key.getSecond());
                }
            });

    public List<Pair<Component, Double>> getTopWithName(Trackable type, int amount) {
        try {
            return nameCache.get(new Pair<>(type, amount));
        } catch (ExecutionException e) {
            errorHandler.handle(e, Severity.WARNING, true);
        }

        return new ArrayList<>();
    }
}
