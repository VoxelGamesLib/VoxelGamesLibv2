package com.voxelgameslib.voxelgameslib.command;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.api.feature.AbstractFeatureCommand;
import com.voxelgameslib.voxelgameslib.internal.handler.Handler;
import com.voxelgameslib.voxelgameslib.api.phase.Phase;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import co.aikar.commands.BukkitCommandManager;

@Singleton
public class CommandHandler implements Handler, Listener {

    private Multimap<String, Phase> commands = HashMultimap.create();

    @Inject
    private BukkitCommandManager commandManager;

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    public void register(@Nonnull AbstractFeatureCommand<?> cmd, @Nonnull Phase phase) {
        commandManager.registerCommand(cmd);
        cmd.getRegisteredRootCommands().keySet().forEach(key -> commands.put(key, phase));
    }

    public void unregister(@Nonnull AbstractFeatureCommand<?> cmd, @Nonnull Phase phase) {
        cmd.getRegisteredRootCommands().keySet().forEach(key -> {
            commands.remove(key, phase);
            if (!commands.containsKey(key)) {
                commandManager.unregisterCommand(cmd);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(@Nonnull PlayerCommandPreprocessEvent event) {
        String label = event.getMessage().split(" ")[0].replace("/", "");
        if (!commands.containsKey(label)) {
            return;
        }

        if (commands.get(label).stream().noneMatch(phase -> phase.getGame().isPlaying(event.getPlayer().getUniqueId()))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
        }
    }
}
