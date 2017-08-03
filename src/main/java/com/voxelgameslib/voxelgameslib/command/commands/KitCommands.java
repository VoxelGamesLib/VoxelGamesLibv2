package com.voxelgameslib.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.voxelgameslib.voxelgameslib.feature.features.KitFeature;
import com.voxelgameslib.voxelgameslib.user.User;
import lombok.extern.java.Log;

import javax.inject.Singleton;

/**
 * Handles commands relating to kits.
 * <p>
 * This is only registered if the {@link KitFeature}
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
