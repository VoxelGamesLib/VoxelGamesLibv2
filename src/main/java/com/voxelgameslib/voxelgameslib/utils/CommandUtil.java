package com.voxelgameslib.voxelgameslib.utils;

import net.kyori.text.TextComponent;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.user.User;

import co.aikar.commands.CommandHelp;

public class CommandUtil {

    public static void printHelp(@Nonnull User user, @Nonnull CommandHelp help) {
        help.getCommandHelp().stream()
                .map(h -> "/" + h.getCommand() + " " + h.getSyntax() +
                        ((h.getHelpText() != null && !h.getHelpText().isEmpty() ? " - " + h.getHelpText() : "")))
                .forEach(s -> user.sendMessage(TextComponent.of(s)));
    }
}
