package com.voxelgameslib.voxelgameslib.phase;

import com.google.gson.annotations.Expose;
import com.google.inject.Injector;

import com.voxelgameslib.voxelgameslib.command.CommandHandler;
import com.voxelgameslib.voxelgameslib.components.ability.Ability;
import com.voxelgameslib.voxelgameslib.event.EventHandler;
import com.voxelgameslib.voxelgameslib.exception.DependencyGraphException;
import com.voxelgameslib.voxelgameslib.exception.NoSuchFeatureException;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeatureCommand;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureCommandImplementor;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.graph.Graph;
import com.voxelgameslib.voxelgameslib.tick.Tickable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import org.bukkit.event.Listener;

import co.aikar.commands.BukkitCommandManager;
import lombok.extern.java.Log;

/**
 * Simple implementation of a {@link Phase}. Implements the necessary {@link Feature}-handling.
 */
@Log
public abstract class AbstractPhase implements Phase {

    @Inject
    private EventHandler eventHandler;
    @Inject
    private BukkitCommandManager commandManager;
    @Inject
    private Injector injector;
    @Inject
    private CommandHandler commandHandler;

    @Expose
    private String name;
    @Expose
    private String className;

    @Expose
    private boolean allowJoin;
    @Expose
    private boolean allowSpectate;

    @Nonnull
    @Expose
    private List<Feature> features = new ArrayList<>();


    private Game game;

    @Nullable
    private Phase nextPhase;
    private boolean isRunning;
    private List<Feature> startedFeatures = new ArrayList<>();

    private LocalDateTime startTime;
    private Duration duration;

    private Map<UUID, Tickable> phaseTickables = new HashMap<>();

    public AbstractPhase() {
        className = getClass().getName().replace(PhaseTypeAdapter.DEFAULT_PATH + ".", "");
    }

    @Override
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setNextPhase(@Nonnull Phase nextPhase) {
        this.nextPhase = nextPhase;
    }

    @Override
    public void setGame(@Nonnull Game game) {
        this.game = game;
    }

    @Override
    public void addFeature(@Nonnull Feature feature) {
        if (features.contains(feature)) {
            throw new RuntimeException("Tried to register " + feature + " twice!");
        }
        log.finer("add " + feature.getClass().getSimpleName() + " feature");
        features.add(feature);
    }

    @Nonnull
    @Override
    public Game getGame() {
        return game;
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Feature> T getFeature(@Nonnull Class<T> clazz) {
        return (T) features.stream().filter(f -> f.getClass().equals(clazz)).findFirst()
                .orElseThrow(() -> new NoSuchFeatureException(clazz));
    }

    @Nonnull
    @Override
    public List<Feature> getFeatures() {
        return features;
    }

    @Nullable
    @Override
    public Phase getNextPhase() {
        return nextPhase;
    }

    @Override
    public void init() {
        log.finer("init " + getName());
    }

    @Override
    public void start() {
        if (!checkDependencies()) {
            game.abortGame();
            return;
        }
        // start timer
        startTime = LocalDateTime.now();

        log.finer("start phase" + getName());

        phaseTickables.values().forEach(Tickable::start);

        for (Feature feature : features) {
            if (game.isAborting()) {
                return;
            }
            log.finer("start " + feature.getName());
            try {
                feature.start();
            } catch (Exception ex) {
                log.severe("error while starting " + feature.getName());
                ex.printStackTrace();
                game.abortGame();
                return;
            }

            if (feature instanceof Listener) {
                eventHandler.registerEvents((Listener) feature, getGame());
            }

            if (feature instanceof FeatureCommandImplementor) {
                AbstractFeatureCommand cmd = injector.getInstance(((FeatureCommandImplementor) feature).getCommandClass());
                //noinspection unchecked
                cmd.setFeature(feature);
                commandHandler.register(cmd, this);
                commandManager.registerCommand(cmd);
            }

            startedFeatures.add(feature);
        }
    }

    @Override
    public void stop() {
        // stop timer
        duration = Duration.between(startTime, LocalDateTime.now());

        log.finer("stop phase " + getName());
        // only stop features that have been started to avoid errors
        for (Feature feature : startedFeatures) {
            log.finer("stop " + feature.getName());
            try {
                feature.stop();
            } catch (Exception ex) {
                log.severe("error while stopping " + feature.getName());
                ex.printStackTrace();
                return;
            }

            if (feature instanceof Listener) {
                eventHandler.unregister((Listener) feature, getGame());
            }

            if (feature instanceof FeatureCommandImplementor) {
                AbstractFeatureCommand cmd = injector.getInstance(((FeatureCommandImplementor) feature).getCommandClass());
                commandHandler.unregister(cmd, this);
                commandManager.unregisterCommand(cmd);
            }
        }

        phaseTickables.values().forEach(tickable -> {
            tickable.stop();

            if (tickable instanceof Ability) {
                ((Ability) tickable).unregister();
            }
        });

        startedFeatures.clear();
    }

    @Override
    public void addTickable(@Nonnull UUID identifier, @Nonnull Tickable tickable) {
        phaseTickables.put(identifier, tickable);
    }

    @Override
    public void removeTickable(@Nonnull UUID identifier) {
        phaseTickables.remove(identifier);
    }

    @Override
    public void tick() {
        features.forEach(Feature::tick);
        phaseTickables.values().forEach(Tickable::tick);
    }

    @Override
    public boolean allowJoin() {
        return allowJoin;
    }

    @Override
    public void setAllowJoin(boolean allowJoin) {
        this.allowJoin = allowJoin;
    }

    @Override
    public boolean allowSpectate() {
        return allowSpectate;
    }

    @Override
    public void setAllowSpectate(boolean allowSpectate) {
        this.allowSpectate = allowSpectate;
    }

    @Override
    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    private boolean checkDependencies() {
        List<Class<? extends Feature>> orderedFeatures = new ArrayList<>();
        List<Class<? extends Feature>> added = new ArrayList<>();
        List<Class<? extends Feature>> missingSoftDependencies = new ArrayList<>();
        try {
            Graph<Class<? extends Feature>> graph = new Graph<>(orderedFeatures::add);

            // add all dependencies to the graph
            for (Feature feature : getFeatures()) {
                for (@SuppressWarnings("unchecked") Class<? extends Feature> dependency : feature
                        .getDependencies()) {
                    if (dependency.equals(feature.getClass())) {
                        log.severe(feature.getName() + " tried to depend on itself...");
                        continue;
                    }
                    graph.addDependency(feature.getClass(), dependency);

                    added.add(feature.getClass());
                    added.add(dependency);

                    try {
                        getFeature(dependency);
                    } catch (NoSuchFeatureException ex) {
                        log.severe("could not find dependency " + dependency.getName() + " for feature " +
                                feature.getClass().getName() + " in phase " + getName());
                        return false;
                    }
                }

                for (@SuppressWarnings("unchecked") Class<? extends Feature> dependency : feature
                        .getSoftDependencies()) {
                    if (dependency.equals(feature.getClass())) {
                        log.severe(feature.getName() + " tried to depend on itself...");
                        continue;
                    }
                    graph.addDependency(feature.getClass(), dependency);

                    added.add(feature.getClass());
                    added.add(dependency);

                    try {
                        getFeature(dependency);
                    } catch (NoSuchFeatureException ex) {
                        missingSoftDependencies.add(dependency);
                    }
                }
            }

            // add features that have no dependency connection to any other feature. they can't be left out alone!
            for (Feature feature : getFeatures()) {
                if (!added.contains(feature.getClass())) {
                    orderedFeatures.add(feature.getClass());
                }
            }
            added.clear();

            // do the magic!
            graph.generateDependencies();
        } catch (DependencyGraphException ex) {
            log.severe("error while trying to generate dependency graph: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }


        // no need to keep stuff that isn't present
        orderedFeatures.removeAll(missingSoftDependencies);

        if (features.size() != orderedFeatures.size()) {
            throw new RuntimeException(
                    "WTF HAPPENED HERE?!" + features.size() + " " + orderedFeatures.size());
        }

        // reverse order because dependencies need to be run before dependend features
        Collections.reverse(orderedFeatures);
        // remap classes to features
        features = orderedFeatures.stream().map((Function<Class, Feature>) this::getFeature)
                .collect(Collectors.toList());

        return true;
    }

    @Override
    @Nonnull
    public Duration getDuration() {
        if (duration == null) {
            return Duration.between(startTime, LocalDateTime.now());
        } else {
            return duration;
        }
    }
}
