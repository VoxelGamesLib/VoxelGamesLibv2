package me.minidigger.voxelgameslib;

import co.aikar.commands.CommandManager;
import co.aikar.timings.lib.TimingManager;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public final class VoxelGamesLibModule extends AbstractModule {

  private VoxelGamesLib voxelGamesLib;
  private TimingManager timingManager;
  private CommandManager commandManager;
  private String pluginVersion;

  public VoxelGamesLibModule(VoxelGamesLib voxelGamesLib,
      TimingManager timingManager, CommandManager commandManager, String pluginVersion) {
    this.voxelGamesLib = voxelGamesLib;
    this.timingManager = timingManager;
    this.commandManager = commandManager;
    this.pluginVersion = pluginVersion;
  }

  @Override
  protected void configure() {
    bind(VoxelGamesLib.class).toInstance(voxelGamesLib);
    bind(TimingManager.class).toInstance(timingManager);
    bind(CommandManager.class).toInstance(commandManager);

    bind(String.class).annotatedWith(Names.named("PluginVersion")).toInstance(pluginVersion);
  }
}
