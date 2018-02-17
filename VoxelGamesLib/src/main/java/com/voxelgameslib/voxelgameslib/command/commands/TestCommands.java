package com.voxelgameslib.voxelgameslib.command.commands;

import net.md_5.bungee.api.ChatColor;

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
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.IncrementalElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;

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

    @Subcommand("chatmenu")
    @CommandPermission("%admin")
    public void chatmenu(@Nonnull User user) {
        ChatMenu menu = new ChatMenu().pauseChat(0, 0, ChatColor.RED + "[Close]");
        menu.add(new TextElement("Hello, world!", 10, 10));
        IncrementalElement incr = new IncrementalElement(5, 5, 10);
        incr.value.setChangeCallback((s) -> System.out.println("IncrementalElement changed! " + s.getPrevious() + " -> " + s.getCurrent()));
        menu.add(incr);
        menu.openFor(user.getPlayer());
    }
}
