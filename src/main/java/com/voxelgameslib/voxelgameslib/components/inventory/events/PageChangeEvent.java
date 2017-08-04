package com.voxelgameslib.voxelgameslib.components.inventory.events;

import com.voxelgameslib.voxelgameslib.components.inventory.PagedInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PageChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private PagedInventory inventory;
    private ItemStack[] contents;
    private int oldPage, newPage;

    public PageChangeEvent(PagedInventory inventory, int oldPage, int newPage, ItemStack[] contents) {
        this.inventory = inventory;
        this.oldPage = oldPage;
        this.newPage = newPage;
        this.contents = contents;
    }

    public PagedInventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return inventory.getPlayer();
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
