package com.voxelgameslib.voxelgameslib.command.commands;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.world.WorldHandler;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;

@Singleton
@CommandAlias("worldrepository|wr")
@CommandPermission("%admin")
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class WorldRepositoryCommands extends BaseCommand {

    @Inject
    private WorldHandler worldHandler;

    @HelpCommand
    @CommandPermission("%admin")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("updateRepo")
    @CommandPermission("%admin")
    public void updateRepo(@Nonnull User sender) {
        worldHandler.getWorldRepository().updateRepo();
        sender.sendMessage(TextComponent.of("[VGL] Repositories updated.").color(TextColor.GREEN));
    }

    @Subcommand("commitRepo")
    @CommandPermission("%admin")
    public void commitRepo(@Nonnull User sender) {
        worldHandler.getWorldRepository().commitRepo();
        sender.sendMessage(TextComponent.of("[VGL] Created commit.").color(TextColor.GREEN));
    }
}
