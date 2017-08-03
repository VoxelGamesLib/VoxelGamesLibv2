package com.voxelgameslib.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.google.inject.name.Named;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@CommandAlias("vgl|voxelgameslib")
public final class VGLCommands extends BaseCommand {

    @Inject
    @Named("PluginVersion")
    private String pluginVersion;

    @Subcommand("version")
    @CommandPermission("%user")
    public void version(CommandSender sender) {
        sender.sendMessage("You are using VoxelGamesLib version " + pluginVersion);
    }
}
