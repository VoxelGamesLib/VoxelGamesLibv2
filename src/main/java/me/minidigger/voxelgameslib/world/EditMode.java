package me.minidigger.voxelgameslib.world;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

/**
 * Commands related to the edit mode
 */
@Singleton
@CommandAlias("editmode")
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class EditMode extends BaseCommand {

    @Nonnull
    private List<UUID> editMode = new ArrayList<>();

    @CommandAlias("editmode")
    @CommandPermission("%admin")
    public void editmode(User sender) {
        //TODO info about edit mode
    }

    @Subcommand("on")
    @CommandPermission("%admin")
    public void on(User sender) {
        if (!editMode.contains(sender.getUuid())) {
            editMode.add(sender.getUuid());
            //TODO item for editmode
            Lang.msg(sender, LangKey.EDITMODE_ENABLED);
        } else {
            Lang.msg(sender, LangKey.EDITMODE_ALREADY_ENABLED);
        }
    }

    @Subcommand("off")
    @CommandPermission("%admin")
    public void off(User sender) {
        if (editMode.contains(sender.getUuid())) {
            editMode.remove(sender.getUuid());
            Lang.msg(sender, LangKey.EDITMODE_DISABLED);
        } else {
            Lang.msg(sender, LangKey.EDITMODE_NOT_ENABLED);
        }
    }

    @Subcommand("skull")
    @CommandPermission("%admin")
    @Syntax("<name> - the name of the skull")
    public void skull(User sender, String name) {
        if (editMode.contains(sender.getUuid())) {
            ItemStack skull = new ItemBuilder(Material.SKULL_ITEM).durability(3).name(name)
                    .meta((itemMeta -> ((SkullMeta) itemMeta).setOwner(name))).build();
            sender.getPlayer().getInventory().setItemInMainHand(skull);
        } else {
            Lang.msg(sender, LangKey.EDITMODE_NOT_ENABLED);
        }
    }

    @Subcommand("chest")
    @CommandPermission("%admin")
    @Syntax("<name> - the name of the chest")
    public void chest(User sender, String name) {
        if (editMode.contains(sender.getUuid())) {
            ItemStack chest = new ItemBuilder(Material.CHEST).name(name).build();
            sender.getPlayer().getInventory().setItemInMainHand(chest);
        } else {
            Lang.msg(sender, LangKey.EDITMODE_NOT_ENABLED);
        }
    }
}
