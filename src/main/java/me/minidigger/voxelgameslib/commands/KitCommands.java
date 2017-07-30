package me.minidigger.voxelgameslib.commands;

import javax.inject.Singleton;

import me.minidigger.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
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
@CommandAlias("kit")
public class KitCommands extends BaseCommand {

    @Default
    @CommandPermission("%user")
    public void showKitMenu(User sender) {
        // todo
    }

    @UnknownHandler
    @Syntax("<kit> - the kit you choose")
    @CommandPermission("%user")
    public void selectKit(User sender, String kit) {
        // todo
    }
}
