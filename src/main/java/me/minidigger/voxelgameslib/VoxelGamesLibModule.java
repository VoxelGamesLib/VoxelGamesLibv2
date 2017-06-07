package me.minidigger.voxelgameslib;

import co.aikar.commands.CommandManager;
import co.aikar.timings.lib.TimingManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import java.io.File;
import me.minidigger.voxelgameslib.config.ConfigHandler;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.feature.FeatureTypeAdapter;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.phase.Phase;
import me.minidigger.voxelgameslib.phase.PhaseTypeAdapter;
import me.minidigger.voxelgameslib.world.WorldConfig;
import me.minidigger.voxelgameslib.world.WorldHandler;
import org.bukkit.Bukkit;

public final class VoxelGamesLibModule extends AbstractModule {

  private VoxelGamesLib voxelGamesLib;
  private TimingManager timingManager;
  private CommandManager commandManager;
  private String pluginVersion;
  private File dataFolder;

  public VoxelGamesLibModule(VoxelGamesLib voxelGamesLib,
      TimingManager timingManager, CommandManager commandManager,
      String pluginVersion, File dataFolder) {
    this.voxelGamesLib = voxelGamesLib;
    this.timingManager = timingManager;
    this.commandManager = commandManager;
    this.pluginVersion = pluginVersion;
    this.dataFolder = dataFolder;
  }

  @Override
  protected void configure() {
    bind(VoxelGamesLib.class).toInstance(voxelGamesLib);
    bind(TimingManager.class).toInstance(timingManager);
    bind(CommandManager.class).toInstance(commandManager);

    bind(String.class).annotatedWith(Names.named("PluginVersion")).toInstance(pluginVersion);

    bind(File.class).annotatedWith(Names.named("ConfigFolder")).toInstance(dataFolder);
    bind(File.class).annotatedWith(Names.named("LangFolder"))
        .toInstance(new File(dataFolder.getAbsoluteFile(), "lang"));
    bind(File.class).annotatedWith(Names.named("WorldsFolder"))
        .toInstance(new File(Bukkit.getWorldContainer().getAbsoluteFile(), "worlds"));
    bind(File.class).annotatedWith(Names.named("WorldContainer"))
        .toInstance(Bukkit.getWorldContainer().getAbsoluteFile());
    bind(File.class).annotatedWith(Names.named("GameDefinitionFolder"))
        .toInstance(new File(dataFolder.getAbsoluteFile(), "games"));
    bind(File.class).annotatedWith(Names.named("DataFolder"))
        .toInstance(new File(dataFolder.getAbsoluteFile(), "data"));

    bind(WorldConfig.class).toProvider(WorldHandler.class);
    bind(GlobalConfig.class).toProvider(ConfigHandler.class);

    requestStaticInjection(Lang.class);
  }

  @Provides
  public Gson getGson(Injector injector) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Phase.class, injector.getInstance(PhaseTypeAdapter.class));
    builder.registerTypeAdapter(Feature.class, injector.getInstance(FeatureTypeAdapter.class));
    builder.setPrettyPrinting();
    builder.excludeFieldsWithoutExposeAnnotation();
    return builder.create();
  }
}
