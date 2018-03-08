package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.stats.StatType;
import com.voxelgameslib.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;

@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("stats")
public class StatsCommands extends BaseCommand {

    @HelpCommand
    @CommandPermission("%admin")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("get")
    @Description("Allows you to view the stats of a user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void get(User sender,
                    @Description("the user which stats should be displayed")
                        User user,
                    @Description("the stats type to display")
                        StatType type) {

    }

    @Subcommand("set")
    @Description("Allows you to set the stats of another user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void set(User sender,
                    @Description("the user which stats should be changed")
                        User user,
                    @Description("the stats type to change")
                        StatType type,
                    @Description("the new amount")
                        int amount) {

    }

    @Subcommand("increment")
    @Description("Allows you to increment the stats of another user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void increment(User sender,
                          @Description("the user which stats should be changed")
                              User user,
                          @Description("the stats type to change")
                              StatType type,
                          @Description("the amount to increment, defaults to 1")
                          @Optional Integer amount) {

    }

    @Subcommand("decrement")
    @Description("Allows you to decrement the stats of another user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void decrement(User sender,
                          @Description("the user which stats should be changed")
                              User user,
                          @Description("the stats type to change")
                              StatType type,
                          @Description("the amount to decrement, defaults to 1")
                          @Optional Integer amount) {

    }
}
