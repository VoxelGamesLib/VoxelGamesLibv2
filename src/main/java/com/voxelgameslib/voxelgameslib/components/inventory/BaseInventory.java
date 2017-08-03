package com.voxelgameslib.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class BaseInventory {
    private UUID identifier;
    private Player player;
    protected Inventory bukkitInventory;
    protected String title;
    protected int size;
    protected Consumer<Player> openInventoryAction = (p) -> {
    };
    protected Consumer<Player> closeInventoryAction = (p) -> {
    };
    protected Map<ItemStack, BiConsumer<ItemStack, InventoryClickEvent>> clickActions = new HashMap<>();

    /**
     * Creates a new BaseInventory
     *
     * @param player the player to create the inventory on
     * @param title  title of new inventory
     * @param size   size/capacity of new inventory
     */
    public BaseInventory(Player player, String title, int size) {
        this.identifier = player.getUniqueId();
        this.player = player;
        this.title = title;
        this.size = Math.min(9, (int) (Math.ceil((double) size / 9)) * 9);

        constructNewInventory();
    }

    /**
     * <p>Get the inventory's identifier</p>
     *
     * <p>This should be the same as the UUID of the player opening it.</p>
     *
     * @return the identifier (generally player UUID)
     */
    public UUID getIdentifier() {
        return identifier;
    }

    /**
     * Returns the inventory holder
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    public Inventory getBukkitInventory() {
        return bukkitInventory;
    }

    /**
     * Set the action to be performed when the inventory opens
     *
     * @param action the action to perform
     */
    public void setOpenAction(Consumer<Player> action) {
        openInventoryAction = action;
    }

    /**
     * Set the action to be performed when the inventory closes
     *
     * @param action the action to perform
     */
    public void setCloseAction(Consumer<Player> action) {
        closeInventoryAction = action;
    }

    /**
     * Add new actions to be performed when a player clicks an ItemStack in an inventory
     *
     * @param is     itemstack to create the click action for
     * @param action the action to perform
     */
    public void addClickAction(ItemStack is, BiConsumer<ItemStack, InventoryClickEvent> action) {
        clickActions.put(is, action);
    }

    /**
     * Perform the defined action when an inventory is opened by a player
     */
    public void onOpen() {
        openInventoryAction.accept(player);
    }

    /**
     * Perform the defined action when an inventory is opened by a player
     */
    public void onClose() {
        closeInventoryAction.accept(player);
    }

    /**
     * Perform the defined action when a certain ItemStack is clicked in an inventory
     *
     * @param is clicked item
     * @param e  click event
     */
    public void onClick(ItemStack is, InventoryClickEvent e) {
        if (clickActions.containsKey(is)) {
            clickActions.get(is).accept(is, e);
        }
    }

    /**
     * Make the player open the inventory
     */
    public void open() {
        player.openInventory(bukkitInventory);
    }

    /**
     * Make the player close the inventory
     */
    public void close() {
        player.closeInventory();
    }

    protected void constructNewInventory() {
        bukkitInventory = Bukkit.createInventory(null, this.size, this.title);
    }
}
