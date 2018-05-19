package com.voxelgameslib.voxelgameslib.phase;

import com.google.gson.annotations.Expose;
import com.google.inject.Injector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.command.CommandHandler;
import com.voxelgameslib.voxelgameslib.components.ability.Ability;
import com.voxelgameslib.voxelgameslib.components.team.Team;
import com.voxelgameslib.voxelgameslib.condition.VictoryCondition;
import com.voxelgameslib.voxelgameslib.condition.conditions.EmptyVictoryCondition;
import com.voxelgameslib.voxelgameslib.event.EventHandler;
import com.voxelgameslib.voxelgameslib.exception.DependencyGraphException;
import com.voxelgameslib.voxelgameslib.exception.NoSuchFeatureException;
import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeatureCommand;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureCommandImplementor;
import com.voxelgameslib.voxelgameslib.feature.features.SpectatorFeature;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.graph.Graph;
import com.voxelgameslib.voxelgameslib.tick.Tickable;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.event.Listener;

import co.aikar.commands.BukkitCommandManager;

/**
 * Simple implementation of a {@link Phase}. Implements the necessary {@link Feature}-handling.
 */
public abstract class AbstractPhase implements Phase {

    private static final Logger log = Logger.getLogger(AbstractPhase.class.getName());
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
    @Nonnull
    @Expose
    private List<VictoryCondition> victoryConditions = new ArrayList<>();

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
    @SuppressWarnings("unchecked")
    public <T extends Feature> Optional<T> getOptionalFeature(@Nonnull Class<T> clazz) {
        return (Optional<T>) features.stream().filter(f -> f.getClass().equals(clazz)).findFirst();
    }

    @Nonnull
    @Override
    public List<Feature> getFeatures() {
        return features;
    }


    @Override
    @Nonnull
    public List<VictoryCondition> getVictoryConditions() {
        return victoryConditions;
    }

    @Override
    public void addVictoryCondition(VictoryCondition victoryCondition) {
        if (victoryConditions.contains(victoryCondition)) {
            throw new RuntimeException("Tried to register " + victoryCondition.getName() + " twice!");
        }
        log.finer("add " + victoryCondition.getName() + " victory condition");
        victoryConditions.add(victoryCondition);
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
    public void enable() {
        if (!checkDependencies()) {
            game.abortGame();
            return;
        }
        if (!checkVictoryConditionDependencies()) {
            game.abortGame();
            return;
        }
        if (victoryConditions.size() == 0) {
            addVictoryCondition(getGame().createVictoryCondition(EmptyVictoryCondition.class, this));
        }

        // check for spec feature
        if (allowSpectate && !getOptionalFeature(SpectatorFeature.class).isPresent()) {
            log.warning(getName() + " does allow spectators but doesn't use the spectator feature! Did you forget to add it?");
        }

        // enable timer
        startTime = LocalDateTime.now();

        log.finer("enable phase" + getName());

        phaseTickables.values().forEach(Tickable::enable);

        for (Feature feature : features) {
            if (game.isAborting()) {
                return;
            }
            log.finer("enable " + feature.getName());
            try {
                feature.enable();
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
            }

            startedFeatures.add(feature);
        }

        for (VictoryCondition victoryCondition : victoryConditions) {
            if (victoryCondition instanceof Listener) {
                eventHandler.registerEvents((Listener) victoryCondition, getGame());
            }
        }
    }

    @Override
    public void disable() {
        // disable timer
        duration = Duration.between(startTime, LocalDateTime.now());

        log.finer("disable phase " + getName());
        // only disable features that have been started to avoid errors
        for (Feature feature : startedFeatures) {
            log.finer("disable " + feature.getName());
            try {
                feature.disable();
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
            }
        }

        for (VictoryCondition victoryCondition : victoryConditions) {
            if (victoryCondition instanceof Listener) {
                eventHandler.registerEvents((Listener) victoryCondition, getGame());
            }
        }

        phaseTickables.values().forEach(tickable -> {
            tickable.disable();

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

        checkEnd();
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
                for (Class<? extends Feature> dependency : feature.getDependencies()) {
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

                for (Class<? extends Feature> dependency : feature.getSoftDependencies()) {
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

            if (graph.size() != 0) {
                // do the magic! (but only if there are actually nodes on the graph)
                graph.generateDependencies();
            }
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

    private boolean checkVictoryConditionDependencies() {
        for (VictoryCondition condition : getVictoryConditions()) {
            for (Class<? extends Feature> feature : condition.getDependencies()) {
                if (!getOptionalFeature(feature).isPresent()) {
                    log.severe(condition.getName() + " defined a dependency on feature " + feature.getSimpleName() + " which isn't present in phase " + getName());
                    return false;
                }
            }
        }
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

    private void checkEnd() {
        // check all victory conditions
        User winner = null;
        Team winnerTeam = null;
        for (VictoryCondition victoryCondition : victoryConditions) {
            if (!victoryCondition.completed()) {
                return;
            }

            if (victoryCondition.getWinner() != null) {
                if (!victoryCondition.getWinner().equals(winner)) {
                    if (winner == null) {
                        if (winnerTeam != null && !winnerTeam.contains(victoryCondition.getWinner())) {
                            throw new VoxelGameLibException(victoryCondition.getName() + " defined a winner even tho we already have a winning team!");
                        }
                        winner = victoryCondition.getWinner();
                    } else {
                        throw new VoxelGameLibException(victoryCondition.getName() + " defined a different winner than one of the conditions before it!");
                    }
                }
            }
            if (victoryCondition.getWinnerTeam() != null) {
                if (!victoryCondition.getWinnerTeam().equals(winnerTeam)) {
                    if (winnerTeam == null) {
                        if (winner != null && !victoryCondition.getWinnerTeam().contains(winner)) {
                            throw new VoxelGameLibException(victoryCondition.getName() + " defined a winning team even tho we already have a winning user!");
                        } else {
                            winnerTeam = victoryCondition.getWinnerTeam();
                        }
                    } else {
                        throw new VoxelGameLibException(victoryCondition.getName() + " defined a different winning team than one of the conditions before it!");
                    }
                }
            }
        }

        // all done, end this game
        getGame().endGame(winnerTeam, winner);
    }
}
