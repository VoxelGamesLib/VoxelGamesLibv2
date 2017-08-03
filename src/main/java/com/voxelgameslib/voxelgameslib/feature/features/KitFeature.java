package com.voxelgameslib.voxelgameslib.feature.features;

import co.aikar.commands.BukkitCommandManager;
import com.google.inject.Injector;
import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.command.commands.KitCommands;
import com.voxelgameslib.voxelgameslib.components.ability.Ability;
import com.voxelgameslib.voxelgameslib.components.kits.Kit;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import javax.inject.Inject;
import java.util.List;

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
