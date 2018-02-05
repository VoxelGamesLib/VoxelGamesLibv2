package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.feature.features.KitFeature;
import com.voxelgameslib.voxelgameslib.user.User;

import lombok.extern.java.Log;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

/**
 * Handles commands relating to kits. <p> This is only registered if the {@link KitFeature} requests for it to be
 * enabled.
 */
@Log
@Singleton
@CommandAlias("kit|k")
public class KitCommands extends BaseCommand {

    @HelpCommand
    @CommandPermission("%user")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @CommandPermission("%user")
    @Subcommand("menu|m")
    public void showKitMenu(@Nonnull User sender) {
        // todo
    }

    @Subcommand("select")
    @CommandPermission("%user")
    @Syntax("<kit> - the kit you choose")
    public void selectKit(@Nonnull User sender, @Nonnull String kit) {
        // todo
    }

    @Subcommand("create")
    @CommandPermission("%admin")
    @Syntax("<kit> - the name of the new kit")
    public void createKit(@Nonnull User user, @Nonnull String kit) {
        // TODO create kit command
    }

    @Subcommand("edit")
    @CommandPermission("%admin")
    @Syntax("<kit> - the name of the kit you want to edit")
    public void editKit(@Nonnull User user, @Nonnull String kit) {
        // TODO edit kit command
    }
}
