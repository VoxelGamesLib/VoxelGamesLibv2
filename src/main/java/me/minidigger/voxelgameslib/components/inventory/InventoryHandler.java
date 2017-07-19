package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.handler.Handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryHandler implements Handler, Listener {

    @Inject
    private VoxelGamesLib voxelGamesLib;

    private Map<UUID, BaseInventory> inventories = new HashMap<>();

    /**
     * @see Handler#start()
     */
    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, voxelGamesLib);
    }

    /**
     * @see Handler#stop()
     */
    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    /**
     * Creates a new Basic Inventory and returns it
     *
     * @param title title of new inventory
     * @param size size/capacity of new inventory
     * @return the newly created inventory
     */
    public BasicInventory createBasicInventory(String title, int size) {
        return new BasicInventory(title, size);
    }

    /**
     * Creates a new inventory for a player and registers it as being active
     *
     * @param inventory the inventory to be used as a base
     * @param player the player to create it for
     * @return an instance of the inventory used when opening it for a player
     */
    public BaseInventory createForPlayer(BaseInventory inventory, Player player) {
        BaseInventory inv = inventory.createForPlayer(player);
        inventories.put(inv.getIdentifier(), inventory);
        return inv;
    }

    /**
     * Removes an inventory from being actively handled by this Handler (pun not intended)
     *
     * @param inventoryId UUID of the inventory (same as the player's UUID generally)
     */
    public void removeInventory(UUID inventoryId) {
        inventories.remove(inventoryId);
    }

    @EventHandler
    public void onOpenListener(InventoryOpenEvent event) {
        event.setCancelled(true);
        inventories.get(event.getPlayer().getUniqueId()).onOpen((Player) event.getPlayer());
    }

    @EventHandler
    public void onCloseListener(InventoryCloseEvent event) {
        inventories.get(event.getPlayer().getUniqueId()).onClose((Player) event.getPlayer());
    }

    @EventHandler
    public void onClickListener(InventoryClickEvent event) {
        event.setCancelled(true);
        inventories.get(event.getWhoClicked().getUniqueId()).onClick(event.getCurrentItem(), event);
    }
}
