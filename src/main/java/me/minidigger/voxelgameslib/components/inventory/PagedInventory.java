package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;

import me.minidigger.voxelgameslib.utils.ItemBuilder;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustEvent;
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

        addClickAction(forward, (itemStack, clickEvent) -> {
            setPage(++currentPage);
        });

        addClickAction(backward, (itemStack, clickEvent) -> {
            setPage(--currentPage);
        });

        addClickAction(close, ((itemStack, clickEvent) -> {
            close((Player) clickEvent.getWhoClicked());
        }));
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

    /**
     * Sets the format for the inventory title.
     *
     * Available placeholders:
     * %title% - replaced with inventory title (defined on object creation)
     * %page% - replaced with page number
     *
     * @param titleFormat format
     */
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

    /**
     * Sets the visible page of the inventory
     *
     * @param newPage id of page to set as visible
     */
    public void setPage(int newPage) {
        if (pages.containsKey(newPage)) {
            currentPage = newPage;

            // do stuff here pls todo yes
        }
    }
}
