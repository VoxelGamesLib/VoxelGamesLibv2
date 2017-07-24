package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;

import me.minidigger.voxelgameslib.utils.ItemBuilder;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
     * @see BaseInventory#BaseInventory(Player, String, int)
     */
    public PagedInventory(Player player, String title, int size) {
        super(player, title, size);
    }

    /**
     * Creates a new PagedInventory
     *
     * Has the ability to specify if the inventory is dynamic (size determined by contents)
     *
     * @see BaseInventory#BaseInventory(Player, String, int)
     */
    public PagedInventory(Player player, String title, boolean dynamicInventory) {
        super(player, title, 54);
        this.dynamicInventory = dynamicInventory;
    }

    public ItemStack getForwardItem() {
        return new ItemBuilder(forward).name(ChatColor.GOLD + "Next").build();
    }

    public ItemStack getBackwardItem() {
        return new ItemBuilder(backward).name(ChatColor.GOLD + "Back").build();
    }

    public ItemStack getCloseItem() {
        return new ItemBuilder(close).name(ChatColor.RED + "Next").build();
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
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
