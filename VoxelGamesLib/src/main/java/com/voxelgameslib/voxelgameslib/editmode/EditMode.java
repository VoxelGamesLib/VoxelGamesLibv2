package com.voxelgameslib.voxelgameslib.editmode;

import org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.map.MapHandler;
import com.voxelgameslib.voxelgameslib.map.MarkerDefinition;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

/**
 * Commands related to the edit mode
 */
@Singleton
@CommandAlias("editmode")
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class EditMode extends BaseCommand {

    @Inject
    private MapHandler mapHandler;

    @Nonnull
    private List<UUID> editMode = new ArrayList<>();

    @Default
    @CommandPermission("%admin")
    public void editmode(@Nonnull User sender) {
        //TODO info about edit mode
    }

    @Subcommand("on")
    @CommandPermission("%admin")
    public void on(@Nonnull User sender) {
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
    public void off(@Nonnull User sender) {
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
    public void skull(@Nonnull User sender, @Nonnull String name) {
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
    public void chest(@Nonnull User sender, @Nonnull String name) {
        if (editMode.contains(sender.getUuid())) {
            ItemStack chest = new ItemBuilder(Material.CHEST).name(name).build();
            sender.getPlayer().getInventory().setItemInMainHand(chest);
        } else {
            Lang.msg(sender, LangKey.EDITMODE_NOT_ENABLED);
        }
    }

    @Subcommand("gui")
    @CommandPermission("%admin")
    public void gui(@Nonnull User sender) {
        if (editMode.contains(sender.getUuid())) {
            //TODO implement paginated invs
            InventoryMenuBuilder builder = new InventoryMenuBuilder().withSize(9).withTitle(Lang.legacy(LangKey.INV_MARKER));
            for (int i = 0; i < mapHandler.getMarkerDefinitions().size(); i++) {
                MarkerDefinition markerDefinition = mapHandler.getMarkerDefinitions().get(i);
                ItemStack is = new ItemBuilder(Material.SKULL_ITEM).durability(3).name(markerDefinition.getPrefix())
                    .meta((itemMeta -> ((SkullMeta) itemMeta).setOwner(markerDefinition.getPrefix()))).build();
                builder.withItem(i, is, (player, clickType, itemStack) -> sender.getPlayer().performCommand("editmode skull " + itemStack.getItemMeta().getDisplayName()), ClickType.LEFT);
            }
            builder.show(sender.getPlayer());
        } else {
            Lang.msg(sender, LangKey.EDITMODE_NOT_ENABLED);
        }
    }
}
