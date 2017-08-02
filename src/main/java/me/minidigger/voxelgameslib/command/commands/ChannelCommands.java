package me.minidigger.voxelgameslib.command.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.chat.ChatHandler;

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
    public void createChannel(CommandSender sender) {
        //
    }

    @Subcommand("delete")
    @CommandPermission("%admin")
    public void deleteChannel(CommandSender sender) {
        //
    }
}
