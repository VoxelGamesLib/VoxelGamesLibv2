package com.voxelgameslib.voxelgameslib.command.commands;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.user.User;

import javax.annotation.Nonnull;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("test")
public class TestCommands extends BaseCommand {

    @Subcommand("chat")
    @CommandPermission("%admin")
    public void chat(@Nonnull User user) {
        Lang.msg(user, LangKey.TEST_KEY);
    }
}
