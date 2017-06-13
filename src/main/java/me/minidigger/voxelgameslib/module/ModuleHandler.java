package me.minidigger.voxelgameslib.module;

import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.timings.Timings;

import lombok.extern.java.Log;

/**
 * The module handler takes care of all modules.
 */
@Log
@Singleton
public class ModuleHandler implements Handler {

    private static List<Class<? extends Module>> offeredModules = new ArrayList<>();
    private static boolean isAcceptingOffers = true;

    @Nonnull
    private List<Module> modules = new ArrayList<>();

    @Inject
    private Injector injector;

    @Override
    public void start() {
        log.info("Loading modules");
        findModules();

        modules.forEach(Module::enable);
    }

    @Override
    public void stop() {
        modules.forEach(Module::disable);
        modules.clear();
    }

    private void findModules() {
        Timings.time("RegisterModules", () -> {
            for (Class<?> clazz : offeredModules) {
                ModuleInfo info = clazz.getAnnotation(ModuleInfo.class);
                if (info == null) {
                    continue; // should not occur
                }
                log.info("Loading module " + info.name() + " v" + info.version() + " by " + Arrays
                        .toString(info.authors()));
                if (Module.class.isAssignableFrom(clazz)) {
                    Module module = injector.getInstance(clazz);
                    this.modules.add(module);
                } else {
                    log.warning("Class " + clazz.getSimpleName()
                            + " has the ModuleInfo annotation but does not implement Module!");
                }
            }

            log.info("Loaded " + this.modules.size() + " modules!");
        });
    }

    public static void offerModule(Class<? extends Module> clazz) {
        if (isAcceptingOffers) {
            offeredModules.add(clazz);
        } else {
            throw new VoxelGameLibException(
                    "Module offers closed! Add 'loadbefore: VoxelGamesLib' to your plugin.yml!");
        }
    }
}
