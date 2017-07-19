package me.minidigger.voxelgameslib.components.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.handler.Handler;

import org.bukkit.Bukkit;
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
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    public void onClickListener(InventoryClickEvent event) {
        // todo
    }
}
