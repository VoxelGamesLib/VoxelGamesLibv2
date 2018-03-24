package com.voxelgameslib.voxelgameslib.stats;

import com.google.inject.Injector;
import com.google.inject.name.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.persistence.PersistenceHandler;
import com.voxelgameslib.voxelgameslib.timings.Timings;
import com.voxelgameslib.voxelgameslib.user.UserHandler;

import org.bukkit.Bukkit;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.SubclassMatchProcessor;

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
    private FastClasspathScanner scanner;

    private List<Stat> statTypes = new ArrayList<>();
    private static List<Trackable> trackables = new ArrayList<>();

    @Override
    public void enable() {
        registerTrackable(StatType.JOIN_COUNT);

        Bukkit.getScheduler().runTaskTimer(vgl, () -> statTypes.stream().filter(Stat::shouldTick).forEach(Stat::tickOneMinute), 20 * 60, 20 * 60);

        Timings.time("RegisterStatTypes",
            () -> scanner.matchSubclassesOf(Stat.class, (SubclassMatchProcessor<Stat>) this::registerStatType).scan());
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
            .filter(trackable -> (trackable.getPrefix() + ":" + trackable.name()).equals(fName))
            .findAny();
    }
}
