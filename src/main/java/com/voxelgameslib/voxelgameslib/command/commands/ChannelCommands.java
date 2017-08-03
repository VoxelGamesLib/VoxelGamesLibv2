package com.voxelgameslib.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import lombok.extern.java.Log;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
@CommandAlias("channel")
public class ChannelCommands extends BaseCommand {
    @Inject
    private ChatHandler chatHandler;

    @Subcommand("create")
    @CommandPermission("%admin")
    public void createChannel(CommandSender sender) {
        //
    }

    @Subcommand("delete")
    @CommandPermission("%admin")
    public void deleteChannel(CommandSender sender) {
        //
    }
}
