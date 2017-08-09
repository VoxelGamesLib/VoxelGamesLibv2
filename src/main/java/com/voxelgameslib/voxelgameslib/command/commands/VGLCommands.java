package com.voxelgameslib.voxelgameslib.command.commands;

import com.google.inject.name.Named;

import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.CommandUtil;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.UnknownHandler;

@CommandAlias("vgl|voxelgameslib")
public final class VGLCommands extends BaseCommand {

    @Inject
    @Named("PluginVersion")
    private String pluginVersion;

    @Default
    @UnknownHandler
    @Subcommand("version")
    @CommandPermission("%user")
    public void version(@Nonnull CommandSender sender) {
        sender.sendMessage("You are using VoxelGamesLib version " + pluginVersion);
    }
}
