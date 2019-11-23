package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.components.user.User;
import com.voxelgameslib.voxelgameslib.internal.lang.Lang;
import com.voxelgameslib.voxelgameslib.internal.lang.LangKey;
import com.voxelgameslib.voxelgameslib.internal.texture.TextureHandler;

import org.bukkit.inventory.ItemStack;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;

@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("texture")
public class TextureCommands extends BaseCommand {

    @Inject
    private TextureHandler textureHandler;

    @HelpCommand
    @CommandPermission("%admin")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("get")
    @CommandPermission("%admin")
    public void getById(@Nonnull User user, @Nonnull Integer id) {
        Lang.msg(user, LangKey.TEXTURE_FETCHING_TEXTURE);
        textureHandler.fetchSkin(id, skin -> {
            ItemStack skull = textureHandler.getSkull(skin);
            user.getPlayer().getInventory().addItem(skull);
            Lang.msg(user, LangKey.TEXTURE_TEXTURE_APPLIED);
        });
    }
}
