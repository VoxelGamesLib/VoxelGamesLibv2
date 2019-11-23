package com.voxelgameslib.voxelgameslib.api.feature.features;

import com.google.gson.annotations.Expose;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.command.commands.KitCommands;
import com.voxelgameslib.voxelgameslib.components.ability.Ability;
import com.voxelgameslib.voxelgameslib.components.kits.Kit;
import com.voxelgameslib.voxelgameslib.components.kits.KitHandler;
import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeature;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import co.aikar.commands.BukkitCommandManager;

/**
 * This feature allows the use of kits and abilities
 */
public class KitFeature extends AbstractFeature {

    private static final Logger log = Logger.getLogger(KitFeature.class.getName());
    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private BukkitCommandManager commandManager;
    @Inject
    private Injector injector;
    @Inject
    private KitHandler kitHandler;

    @Expose
    private List<String> allowedKits = new ArrayList<>();

    private List<Kit> kits = new ArrayList<>();
    private boolean registerCommands;

    /**
     * @see AbstractFeature#enable()
     */
    @Override
    public void enable() {
        if (registerCommands) {
            commandManager.registerCommand(injector.getInstance(KitCommands.class));
        }

        if (allowedKits.size() == 0) {
            allowedKits.add("DefaultKit");
        }

        // try to load kits
        allowedKits.forEach(kit -> {
            Optional<Kit> k = kitHandler.loadKit(kit);
            if (k.isPresent()) {
                kits.add(k.get());
            } else {
                log.warning("Could not find kit " + kit);
            }
        });

        kits.forEach(kit -> {
            if (kit.getAbilities() != null) {
                for (Ability ability : kit.getAbilities().values()) {
                    Bukkit.getPluginManager().registerEvents(ability, voxelGamesLib);
                }
            }
        });
    }

    /**
     * @see AbstractFeature#disable()
     */
    @Override
    public void disable() {
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
     * Adds a kit to the feature
     *
     * @param kit kit to add
     */
    public void addKit(@Nonnull Kit kit) {
        kits.add(kit);
    }

    /**
     * Set the kits for the feature
     *
     * @param kits list containing kits
     */
    public void setKits(@Nonnull List<Kit> kits) {
        this.kits = kits;
    }

    public boolean isRegisterCommands() {
        return this.registerCommands;
    }

    public void setRegisterCommands(boolean registerCommands) {
        this.registerCommands = registerCommands;
    }
}
