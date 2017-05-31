package me.minidigger.voxelgameslib;

import co.aikar.commands.ACF;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandManager;
import co.aikar.timings.lib.MCTiming;
import co.aikar.timings.lib.TimingManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public final class VoxelGamesLib extends JavaPlugin {

  private TimingManager timingManager;
  private CommandManager commandManager;
  private Injector injector;

  @Override
  public void onEnable() {
    // Plugin startup logic

    // timings
    timingManager = TimingManager.of(this);

    // commands
    commandManager = ACF.createManager(this);

    // guice
    VoxelGamesLibModule voxelGamesLibModule = new VoxelGamesLibModule(this, timingManager,
        commandManager, getDescription().getVersion());
    injector = Guice.createInjector(voxelGamesLibModule);

    // register commands
    registerCommands();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    timingManager = null;
    commandManager = null;
    injector = null;
  }

  private void registerCommands() {
    try (MCTiming ignored = timingManager.of("RegisterCommands")) {
      new Reflections("").getSubTypesOf(BaseCommand.class).stream()
          .filter(clazz -> !clazz.getPackage().getName().equals(
              "me.minidigger.voxelgameslib.command")) // don't register acf internal commands
          .filter(clazz -> clazz.getEnclosingClass() == null) // only register top level commands
          .forEach(clazz -> commandManager.registerCommand(injector.getInstance(clazz)));
    }
  }
}
