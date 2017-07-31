package me.minidigger.voxelgameslib.commands;

import javax.inject.Singleton;

import me.minidigger.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.UnknownHandler;
import lombok.extern.java.Log;

/**
 * Handles commands relating to kits.
 *
 * This is only registered if the {@link me.minidigger.voxelgameslib.feature.features.KitFeature}
 * requests for it to be enabled.
 */
@Log
@Singleton
@CommandAlias("kit|k")
public class KitCommands extends BaseCommand {

    @Default
    @UnknownHandler
    @CommandPermission("%user")
    @Subcommand("menu|m")
    public void showKitMenu(User sender) {
        // todo
    }

    @Subcommand("select")
    @CommandPermission("%user")
    @Syntax("<kit> - the kit you choose")
    public void selectKit(User sender, String kit) {
        // todo
    }

    @Subcommand("create")
    @CommandPermission("%admin")
    @Syntax("<kit> - the name of the new kit")
    public void createKit(User user, String kit) {
        // TODO create kit command
    }

    @Subcommand("edit")
    @CommandPermission("%admin")
    @Syntax("<kit> - the name of the kit you want to edit")
    public void editKit(User user, String kit) {
        // TODO edit kit command
    }
}
