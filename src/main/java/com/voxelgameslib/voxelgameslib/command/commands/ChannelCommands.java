package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.command.CommandSender;

import lombok.extern.java.Log;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;

@Log
@Singleton
@CommandAlias("channel")
public class ChannelCommands extends BaseCommand {

    @Inject
    private ChatHandler chatHandler;

    @HelpCommand
    @CommandPermission("%user")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @CommandPermission("%admin")
    public void createChannel(@Nonnull CommandSender sender) {
        // TODO chat channel commands
    }

    @Subcommand("delete")
    @CommandPermission("%admin")
    public void deleteChannel(@Nonnull CommandSender sender) {
        //
    }
}
