package com.voxelgameslib.voxelgameslib.components.inventory;

import com.google.inject.Injector;
import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
@Log
public class InventoryHandler implements Handler, Listener {

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private Injector injector;

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
     * Creates a new inventory for a player
     *
     * @param inventoryType Type of inventory to use
     * @param player        player to create the inventory for
     * @param title         inventory title
     * @param size          size of inventory space
     * @return the created inventory
     */
    public <T extends BaseInventory> T createInventory(Class<T> inventoryType, Player player, String title, int size) {
        T instance = null;

        try {
            instance = inventoryType.getDeclaredConstructor(Player.class, String.class, int.class).newInstance(player, title, size);
            injector.injectMembers(instance);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.severe("Error creating new inventory (VGL Inventory API): " + e.getMessage());
            e.printStackTrace();
        }

        inventories.put(player.getUniqueId(), instance);

        return instance;
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
        this.getInventory(event.getPlayer()).ifPresent(BaseInventory::onOpen);
    }

    @EventHandler
    public void onCloseListener(InventoryCloseEvent event) {
        this.getInventory(event.getPlayer()).ifPresent(BaseInventory::onClose);
    }

    @EventHandler
    public void onClickListener(InventoryClickEvent event) {
        this.getInventory(event.getWhoClicked()).ifPresent(inventory -> {
            event.setCancelled(true);
            inventory.onClick(event.getCurrentItem(), event);
        });
    }

    private Optional<BaseInventory> getInventory(final HumanEntity player) {
        return Optional.ofNullable(this.inventories.get(player.getUniqueId()));
    }
}
