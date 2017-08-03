package com.voxelgameslib.voxelgameslib.components.inventory;

import org.bukkit.entity.Player;

/**
 * A basic inventory is a type of inventory.
 * <p>
 * It has only one page. Clicking an item will perform an action. There will be no extra pages other
 * than the single inventory page.
 */
public class BasicInventory extends BaseInventory {
    public BasicInventory(Player player, String title, int size) {
        super(player, title, size);
    }
}
