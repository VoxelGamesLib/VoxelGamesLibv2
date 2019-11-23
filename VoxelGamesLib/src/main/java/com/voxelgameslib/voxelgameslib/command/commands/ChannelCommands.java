package com.voxelgameslib.voxelgameslib.command.commands;

import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.components.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.components.user.User;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;

@Singleton
@CommandAlias("channel")
public class ChannelCommands extends BaseCommand {

    private static final Logger log = Logger.getLogger(ChannelCommands.class.getName());
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
