package me.minidigger.voxelgameslib.phase;

import co.aikar.commands.CommandManager;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.exception.DependencyGraphException;
import me.minidigger.voxelgameslib.exception.NoSuchFeatureException;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.graph.Graph;

/**
 * Simple implementation of a {@link Phase}. Implements the necessary {@link Feature}-handling.
 */
@Log
public abstract class AbstractPhase implements Phase {

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

  @Inject
  private CommandManager commandManager;

  private Game game;

  private Phase nextPhase;
  private boolean isRunning;
  private List<Feature> startedFeatures = new ArrayList<>();

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
  public void setNextPhase(Phase nextPhase) {
    this.nextPhase = nextPhase;
  }

  @Override
  public void setGame(@Nonnull Game game) {
    this.game = game;
  }

  @Override
  public void addFeature(@Nonnull Feature feature) {
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

  @Nonnull
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
    log.finer("start phase" + getName());
    for (Feature feature : features) {
      log.finer("start " + feature.getName());
      try {
        feature.start();
      } catch (Exception ex) {
        log.severe("error while starting " + feature.getName());
        ex.printStackTrace();
        game.abortGame();
        return;
      }
      eventHandler.registerEvents(feature);
      commandManager.registerCommand(features);
      startedFeatures.add(feature);
    }

    eventHandler.registerEvents(this);
    commandManager.registerCommand(this);
  }

  @Override
  public void stop() {
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
      eventHandler.unregisterEvents(feature);
      commandManager.unregister(feature, true);
    }
    startedFeatures.clear();

    eventHandler.unregisterEvents(this);
    commandManager.unregister(this, true);
  }

  @Override
  public void tick() {
    features.forEach(Feature::tick);
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

  @SuppressWarnings("JavaDoc")
  public void skip(@Nonnull CommandArguments arguments) {
    if (getGame().isPlaying(arguments.getSender()) || getGame()
        .isSpectating(arguments.getSender())) {
      log.finer("skip " + getName());
      getGame().endPhase();
    }
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
}
