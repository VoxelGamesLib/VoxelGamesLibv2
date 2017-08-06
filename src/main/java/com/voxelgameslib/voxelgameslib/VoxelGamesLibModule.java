package com.voxelgameslib.voxelgameslib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureTypeAdapter;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.game.GameTypeAdapter;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.log.LoggingHandler;
import com.voxelgameslib.voxelgameslib.module.Module;
import com.voxelgameslib.voxelgameslib.phase.Phase;
import com.voxelgameslib.voxelgameslib.phase.PhaseTypeAdapter;
import com.voxelgameslib.voxelgameslib.role.Permission;
import com.voxelgameslib.voxelgameslib.world.WorldConfig;
import com.voxelgameslib.voxelgameslib.world.WorldHandler;

import java.io.File;
import java.util.Map;
import javax.annotation.Nonnull;

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

    @Nonnull
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
        bind(File.class).annotatedWith(Names.named("SkinsFolder"))
                .toInstance(new File(dataFolder.getAbsoluteFile(), "skins"));

        bind(WorldConfig.class).toProvider(WorldHandler.class);
        bind(GlobalConfig.class).toProvider(ConfigHandler.class);

        requestStaticInjection(Lang.class);
        requestStaticInjection(Permission.class);

        offeredModules.forEach((key, value) -> bind(key).toInstance(value));
    }

    @Provides
    @Nonnull
    public Gson getGson(@Nonnull Injector injector) {
        GsonBuilder builder = new GsonBuilder();
        addTypeAdapters(builder, injector);
        builder.setPrettyPrinting();
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    @Provides
    @Named("IgnoreExposedBS")
    @Nonnull
    public Gson getGsonWithoutExposed(@Nonnull Injector injector) {
        GsonBuilder builder = new GsonBuilder();
        addTypeAdapters(builder, injector);
        builder.setPrettyPrinting();
        return builder.create();
    }

    private void addTypeAdapters(@Nonnull GsonBuilder builder, @Nonnull Injector injector) {
        builder.registerTypeAdapter(Phase.class, injector.getInstance(PhaseTypeAdapter.class));
        builder.registerTypeAdapter(Feature.class, injector.getInstance(FeatureTypeAdapter.class));
        builder.registerTypeAdapter(Game.class, injector.getInstance(GameTypeAdapter.class));
    }
}
