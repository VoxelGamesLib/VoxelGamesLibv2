package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.stats.StatInstance;
import com.voxelgameslib.voxelgameslib.stats.Trackable;
import com.voxelgameslib.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Flags;
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
                    @Flags("other,defaultself") User user,
                    @Description("the stats type to display")
                        Trackable type) {
        double val = user.getUserData().getStat(type).getVal();
        Lang.msg(sender, LangKey.STATS_GET, user.getDisplayName(), type.formatLong(val, sender.getLocale()));
    }

    @Subcommand("set")
    @Description("Allows you to set the stats of another user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void set(User sender,
                    @Description("the user which stats should be changed")
                    @Flags("other") User user,
                    @Description("the stats type to change")
                        Trackable type,
                    @Description("the new amount")
                        double amount) {
        StatInstance stat = user.getUserData().getStat(type);
        stat.setVal(amount);
        Lang.msg(sender, LangKey.STATS_SET, user.getDisplayName(), type.getDisplayName(), type.formatShort(stat.getVal()));
    }

    @Subcommand("increment")
    @Description("Allows you to increment the stats of another user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void increment(User sender,
                          @Description("the user which stats should be changed")
                          @Flags("other") User user,
                          @Description("the stats type to change")
                              Trackable type,
                          @Description("the amount to increment, defaults to 1")
                          @Default("1") int amount) {
        StatInstance stat = user.getUserData().getStat(type);
        stat.increment(amount);
        Lang.msg(sender, LangKey.STATS_INCREMENT, user.getDisplayName(), type.getDisplayName(), type.formatLong(stat.getVal(), sender.getLocale()));
    }

    @Subcommand("decrement")
    @Description("Allows you to decrement the stats of another user")
    @CommandPermission("%admin")
    @CommandCompletion("@players @stats")
    public void decrement(User sender,
                          @Description("the user which stats should be changed")
                          @Flags("other") User user,
                          @Description("the stats type to change")
                              Trackable type,
                          @Description("the amount to decrement, defaults to 1")
                          @Default("1") int amount) {
        StatInstance stat = user.getUserData().getStat(type);
        stat.decrement(amount);
        Lang.msg(sender, LangKey.STATS_DECREMENT, user.getDisplayName(), type.getDisplayName(), type.formatLong(stat.getVal(), sender.getLocale()));
    }
}
