package com.voxelgameslib.voxelgameslib.command.commands;

import com.voxelgameslib.voxelgameslib.user.User;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.world.WorldHandler;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;

@Singleton
@CommandAlias("worldrepository|wr")
@CommandPermission("%admin")
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class WorldRepositoryCommands extends BaseCommand {

    @Inject
    private WorldHandler worldHandler;

    @Subcommand("updateRepo")
    public void updateRepo(User sender) {
        worldHandler.getWorldRepository().updateRepo();
        sender.sendMessage(TextComponent.of("[VGL] Repositories updated.").color(TextColor.GREEN));
    }

    @Subcommand("commitRepo")
    public void commitRepo(User sender) {
        worldHandler.getWorldRepository().commitRepo();
        sender.sendMessage(TextComponent.of("[VGL] Created commit.").color(TextColor.GREEN));
    }
}
