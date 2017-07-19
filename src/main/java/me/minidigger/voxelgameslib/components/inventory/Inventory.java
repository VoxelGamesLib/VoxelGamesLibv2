package me.minidigger.voxelgameslib.components.inventory;

import java.util.function.BiConsumer;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

interface Inventory {

    /**
     * Acts when an inventory is clicked
     */
    void onClick(BiConsumer<ItemStack, InventoryClickEvent> action);
}
