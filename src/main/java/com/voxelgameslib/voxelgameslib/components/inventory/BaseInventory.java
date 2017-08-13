package com.voxelgameslib.voxelgameslib.components.inventory;

import com.voxelgameslib.voxelgameslib.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BaseInventory {

    private UUID identifier;
    private User user;
    protected Inventory bukkitInventory;
    protected String title;
    protected int size;
    protected Consumer<User> openInventoryAction = (p) -> {
    };
    protected Consumer<User> closeInventoryAction = (p) -> {
    };
    protected Map<ItemStack, BiConsumer<ItemStack, User>> clickActions = new HashMap<>();

    /**
     * Creates a new BaseInventory
     *
     * @param user  the player to create the inventory on
     * @param title title of new inventory
     * @param size  size/capacity of new inventory
     */
    public BaseInventory(@Nonnull User user, @Nonnull String title, int size) {
        this.identifier = user.getUuid();
        this.user = user;
        this.title = title;
        this.size = Math.min(9, (int) (Math.ceil((double) size / 9)) * 9);

        constructNewInventory();
    }

    /**
     * <p>Get the inventory's identifier</p> <p> <p>This should be the same as the UUID of the player opening it.</p>
     *
     * @return the identifier (generally player UUID)
     */
    @Nonnull
    public UUID getIdentifier() {
        return identifier;
    }

    /**
     * Returns the inventory holder
     *
     * @return the player
     */
    @Nonnull
    public User getUser() {
        return user;
    }

    @Nonnull
    public Inventory getBukkitInventory() {
        return bukkitInventory;
    }

    /**
     * Set the action to be performed when the inventory opens
     *
     * @param action the action to perform
     */
    public void setOpenAction(@Nonnull Consumer<User> action) {
        openInventoryAction = action;
    }

    /**
     * Set the action to be performed when the inventory closes
     *
     * @param action the action to perform
     */
    public void setCloseAction(@Nonnull Consumer<User> action) {
        closeInventoryAction = action;
    }

    /**
     * Add new actions to be performed when a player clicks an ItemStack in an inventory
     *
     * @param is     itemstack to create the click action for
     * @param action the action to perform
     */
    public void addClickAction(@Nonnull ItemStack is, @Nonnull BiConsumer<ItemStack, User> action) {
        clickActions.put(is, action);
    }

    /**
     * Perform the defined action when an inventory is opened by a player
     */
    public void onOpen() {
        openInventoryAction.accept(user);
    }

    /**
     * Perform the defined action when an inventory is opened by a player
     */
    public void onClose() {
        closeInventoryAction.accept(user);
    }

    /**
     * Perform the defined action when a certain ItemStack is clicked in an inventory
     *
     * @param is clicked item
     * @param e  click event
     */
    public void onClick(@Nonnull ItemStack is, @Nonnull InventoryClickEvent e) {
        if (clickActions.containsKey(is)) {
            clickActions.get(is).accept(is, user);
        }
    }

    /**
     * Make the player open the inventory
     */
    public void open() {
        user.getPlayer().openInventory(bukkitInventory);
    }

    /**
     * Make the player close the inventory
     */
    public void close() {
        user.getPlayer().closeInventory();
    }

    protected void constructNewInventory() {
        bukkitInventory = Bukkit.createInventory(null, this.size, this.title);
    }
}
