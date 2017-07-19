package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class BaseInventory {
    private UUID identifier;
    protected Inventory bukkitInventory;
    protected String title;
    protected int size;
    protected Consumer<Player> openInventoryAction = (p) -> {};
    protected Consumer<Player> closeInventoryAction = (p) -> {};
    protected Map<ItemStack, BiConsumer<ItemStack, InventoryClickEvent>> clickActions = new HashMap<>();

    /**
     * Creates a new BaseInventory
     *
     * @param title title of new inventory
     * @param size size/capacity of new inventory
     */
    public BaseInventory(String title, int size) {
        this.title = title;
        this.size = Math.min(54, (int) (Math.ceil((double) size / 9)) * 9);

        bukkitInventory = Bukkit.createInventory(null, size, title);
    }

    /**
     * Creates a new instance of this inventory designed to be used by the player
     *
     * @param p player that will open the inventory
     * @return the duplicated inventory
     */
    BaseInventory createForPlayer(Player p) {
        BaseInventory inv = null;
        try {
            inv = (BaseInventory) clone();
            inv.setIdentifier(p.getUniqueId());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return inv;
    }

    /**
     * <p>Get the inventory's identifier</p>
     *
     * <p>This should be the same as the UUID of the player opening it.</p>
     *
     * @return
     */
    public UUID getIdentifier() {
        return identifier;
    }

    /**
     * <p>Sets the identifier for this inventory</p>
     *
     * <p>This should be the same as the player opening it.</p>
     *
     * @param identifier identifier of the inventory
     */
    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
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
     * @param is itemstack to create the click action for
     * @param action the action to perform
     */
    public void addClickAction(ItemStack is, BiConsumer<ItemStack, InventoryClickEvent> action) {
        clickActions.put(is, action);
    }

    /**
     * Perform the defined action when an inventory is opened by a player
     */
    public void onOpen(Player p) {
        openInventoryAction.accept(p);
    }

    /**
     * Perform the defined action when an inventory is opened by a player
     */
    public void onClose(Player p) {
        closeInventoryAction.accept(p);
    }

    /**
     * Perform the defined action when a certain ItemStack is clicked in an inventory
     *
     * @param is clicked item
     * @param e click event
     */
    public void onClick(ItemStack is, InventoryClickEvent e) {
        if (clickActions.containsKey(is)) {
            clickActions.get(is).accept(is, e);
        }
    }
}
