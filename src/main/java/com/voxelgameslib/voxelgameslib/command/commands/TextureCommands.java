package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.texture.TextureHandler;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.CommandUtil;

import org.bukkit.inventory.ItemStack;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.UnknownHandler;

@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("texture")
public class TextureCommands extends BaseCommand {

    @Inject
    private TextureHandler textureHandler;

    @Default
    @UnknownHandler
    @Subcommand("help")
    @CommandPermission("%admin")
    public void help(@Nonnull User sender) {
        CommandUtil.printHelp(sender, getCommandHelp());
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
