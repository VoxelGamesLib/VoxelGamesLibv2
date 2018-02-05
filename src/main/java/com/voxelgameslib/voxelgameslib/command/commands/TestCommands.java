package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("test")
public class TestCommands extends BaseCommand {

    @HelpCommand
    @CommandPermission("%admin")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("chat")
    @CommandPermission("%admin")
    public void chat(@Nonnull User user) {
        Lang.msg(user, LangKey.TEST_KEY);
    }
}
