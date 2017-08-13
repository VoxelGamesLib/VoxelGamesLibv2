package com.voxelgameslib.voxelgameslib.components.inventory;

import com.google.inject.Injector;
import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.user.User;
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

import javax.annotation.Nonnull;
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
     * @param user          player to create the inventory for
     * @param title         inventory title
     * @param size          size of inventory space
     * @return the created inventory
     */
    @Nonnull
    public <T extends BaseInventory> T createInventory(@Nonnull Class<T> inventoryType, @Nonnull User user, @Nonnull String title, int size) {
        T instance = null;

        try {
            instance = inventoryType.getDeclaredConstructor(User.class, String.class, int.class).newInstance(user, title, size);
            injector.injectMembers(instance);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new VoxelGameLibException("Error creating new inventory (VGL Inventory API): " + e.getMessage(), e);
        }

        inventories.put(user.getUuid(), instance);

        return instance;
    }

    /**
     * Removes an inventory from being actively handled by this Handler (pun not intended)
     *
     * @param inventoryId UUID of the inventory (same as the player's UUID generally)
     */
    public void removeInventory(@Nonnull UUID inventoryId) {
        inventories.remove(inventoryId);
    }

    @EventHandler
    public void onOpenListener(@Nonnull InventoryOpenEvent event) {
        this.getInventory(event.getPlayer()).ifPresent(BaseInventory::onOpen);
    }

    @EventHandler
    public void onCloseListener(@Nonnull InventoryCloseEvent event) {
        this.getInventory(event.getPlayer()).ifPresent(BaseInventory::onClose);
    }

    @EventHandler
    public void onClickListener(@Nonnull InventoryClickEvent event) {
        this.getInventory(event.getWhoClicked()).ifPresent(inventory -> {
            event.setCancelled(true);
            inventory.onClick(event.getCurrentItem(), event);
        });
    }

    @Nonnull
    private Optional<BaseInventory> getInventory(@Nonnull final HumanEntity player) {
        return Optional.ofNullable(this.inventories.get(player.getUniqueId()));
    }
}
