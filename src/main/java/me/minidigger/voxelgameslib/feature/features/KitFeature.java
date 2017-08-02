package me.minidigger.voxelgameslib.feature.features;

import com.google.inject.Injector;

import java.util.List;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.command.commands.KitCommands;
import me.minidigger.voxelgameslib.components.ability.Ability;
import me.minidigger.voxelgameslib.components.kits.Kit;
import me.minidigger.voxelgameslib.feature.AbstractFeature;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import co.aikar.commands.BukkitCommandManager;
import lombok.Getter;
import lombok.Setter;

/**
 * This feature allows the use of kits and abilities
 */
public class KitFeature extends AbstractFeature {

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private BukkitCommandManager commandManager;
    @Inject
    private Injector injector;

    private List<Kit> kits;
    @Getter
    @Setter
    private boolean registerCommands;

    /**
     * @see AbstractFeature#start()
     */
    @Override
    public void start() {
        if (registerCommands) {
            commandManager.registerCommand(injector.getInstance(KitCommands.class));
        }

        kits.forEach(kit -> {
            if (kit.getAbilities() != null) {
                for (Ability ability : kit.getAbilities().values()) {
                    Bukkit.getPluginManager().registerEvents(ability, voxelGamesLib);
                }
            }
        });
    }

    /**
     * @see AbstractFeature#stop()
     */
    @Override
    public void stop() {
        if (registerCommands) {
            commandManager.unregisterCommand(injector.getInstance(KitCommands.class));
        }

        kits.forEach(kit -> {
            if (kit.getAbilities() != null) {
                for (Ability ability : kit.getAbilities().values()) {
                    HandlerList.unregisterAll(ability);
                }
            }
        });
    }

    /**
     * @see AbstractFeature#tick()
     */
    @Override
    public void tick() {

    }

    /**
     * @see AbstractFeature#init()
     */
    @Override
    public void init() {

    }

    /**
     * Adds a kit to the feature
     *
     * @param kit kit to add
     */
    public void addKit(Kit kit) {
        kits.add(kit);
    }

    /**
     * Set the kits for the feature
     *
     * @param kits list containing kits
     */
    public void setKits(List<Kit> kits) {
        this.kits = kits;
    }

    /**
     * @see AbstractFeature#getDependencies()
     */
    @Override
    public Class[] getDependencies() {
        return new Class[0];
    }
}
