package com.voxelgameslib.voxelgameslib.command.commands;

import com.voxelgameslib.voxelgameslib.chat.ChatHandler;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.extern.java.Log;

@Log
@Singleton
@CommandAlias("channel")
public class ChannelCommands extends BaseCommand {
    @Inject
    private ChatHandler chatHandler;

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
