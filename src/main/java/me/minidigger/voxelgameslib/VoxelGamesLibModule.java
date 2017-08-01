package me.minidigger.voxelgameslib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import java.io.File;
import java.util.Map;

import me.minidigger.voxelgameslib.config.ConfigHandler;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.feature.FeatureTypeAdapter;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.game.GameTypeAdapter;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.log.LoggingHandler;
import me.minidigger.voxelgameslib.module.Module;
import me.minidigger.voxelgameslib.phase.Phase;
import me.minidigger.voxelgameslib.phase.PhaseTypeAdapter;
import me.minidigger.voxelgameslib.role.Permission;
import me.minidigger.voxelgameslib.world.WorldConfig;
import me.minidigger.voxelgameslib.world.WorldHandler;

import org.bukkit.Bukkit;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.timings.lib.TimingManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class VoxelGamesLibModule extends AbstractModule {

    private VoxelGamesLib voxelGamesLib;
    private LoggingHandler loggingHandler;
    private TimingManager timingManager;
    private BukkitCommandManager commandManager;
    private String pluginVersion;
    private File dataFolder;
    private Map<Class<Module>, Module> offeredModules;

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        bind(VoxelGamesLib.class).toInstance(voxelGamesLib);
        bind(TimingManager.class).toInstance(timingManager);
        bind(BukkitCommandManager.class).toInstance(commandManager);
        bind(LoggingHandler.class).toInstance(loggingHandler);

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
        bind(File.class).annotatedWith(Names.named("KitsFolder"))
                .toInstance(new File(dataFolder.getAbsoluteFile(), "kits"));

        bind(WorldConfig.class).toProvider(WorldHandler.class);
        bind(GlobalConfig.class).toProvider(ConfigHandler.class);

        requestStaticInjection(Lang.class);
        requestStaticInjection(Permission.class);

        offeredModules.forEach((key, value) -> bind(key).toInstance(value));
    }

    @Provides
    public Gson getGson(Injector injector) {
        GsonBuilder builder = new GsonBuilder();
        addTypeAdapters(builder, injector);
        builder.setPrettyPrinting();
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    @Provides
    @Named("IgnoreExposedBS")
    public Gson getGsonWithoutExposed(Injector injector) {
        GsonBuilder builder = new GsonBuilder();
        addTypeAdapters(builder, injector);
        builder.setPrettyPrinting();
        return builder.create();
    }

    private void addTypeAdapters(GsonBuilder builder, Injector injector) {
        builder.registerTypeAdapter(Phase.class, injector.getInstance(PhaseTypeAdapter.class));
        builder.registerTypeAdapter(Feature.class, injector.getInstance(FeatureTypeAdapter.class));
        builder.registerTypeAdapter(Game.class, injector.getInstance(GameTypeAdapter.class));
    }
}
