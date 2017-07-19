package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryHandler implements Handler, Listener {

    @Inject
    private VoxelGamesLib voxelGamesLib;

    private Map<UUID, Inventory> inventories = new HashMap<>();

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, voxelGamesLib);

        Inventory inventory = new Inventory();
        inventory.addAction(new ItemBuilder(Material.MAGMA_CREAM).name("Click me").build(),
                (is, event) -> event.getWhoClicked().sendMessage("You clicked " + is.getItemMeta().getDisplayName()));
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClickListener(InventoryClickEvent event) {
        // todo
        Inventory inv = null; // todo get the inv
        inv.onClick(event.getCurrentItem(), event);
    }
}
