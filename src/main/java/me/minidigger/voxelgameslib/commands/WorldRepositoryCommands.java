package me.minidigger.voxelgameslib.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.world.WorldHandler;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;

@Singleton
@CommandAlias("worldrepository|wr")
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class WorldRepositoryCommands extends BaseCommand {

    @Inject
    private WorldHandler worldHandler;

    @Subcommand("updateRepo")
    public void updateRepo(User sender) {
        worldHandler.getWorldRepository().updateRepo();
    }

    @Subcommand("commitRepo")
    public void commitRepo(User user) {
        worldHandler.getWorldRepository().commitRepo();
    }
}
