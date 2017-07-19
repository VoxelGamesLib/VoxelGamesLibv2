package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;

import me.minidigger.voxelgameslib.utils.ItemBuilder;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * A PagedInventory is a type of inventory.
 *
 * It has support for pagination, so you can have a multi-paged inventory.
 */
public class PagedInventory extends BaseInventory {
    private ItemStack forward, backward, close;
    private int currentPage;
    private boolean dynamicInventory = true;
    private String titleFormat = "%title% - Page %page%";

    private Map<Integer, ItemStack[]> pages = new HashMap<>();

    /**
     * Creates a new PagedInventory
     *
     * @param title title of new inventory
     * @param size  size/capacity of new inventory
     */
    public PagedInventory(String title, int size) {
        super(title, size);
    }

    public PagedInventory(String title, boolean dynamicInventory) {
        super(title, 54);
        this.dynamicInventory = dynamicInventory;
    }

    public ItemStack getForwardItem() {
        return new ItemBuilder(forward).name(ChatColor.GOLD + "Next").build();
    }

    public ItemStack getBackwardItem() {
        return new ItemBuilder(forward).name(ChatColor.GOLD + "Back").build();
    }

    public ItemStack getCloseItem() {
        return new ItemBuilder(forward).name(ChatColor.RED + "Next").build();
    }

    public void setForwardItem(ItemStack item) {
        forward = item;
    }

    public void setBackwardItem(ItemStack item) {
        backward = item;
    }

    public void setCloseItem(ItemStack item) {
        close = item;
    }
}
