package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Inventory {

    private Map<ItemStack, BiConsumer<ItemStack, InventoryClickEvent>> actions = new HashMap<>();

    public void addAction(ItemStack is, BiConsumer<ItemStack, InventoryClickEvent> action) {
        actions.put(is, action);
    }

    public void onClick(ItemStack is, InventoryClickEvent e) {
        if (actions.containsKey(is)) {
            actions.get(is).accept(is, e);
        }
    }
}
