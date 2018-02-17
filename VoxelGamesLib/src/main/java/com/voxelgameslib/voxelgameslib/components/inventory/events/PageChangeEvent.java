package com.voxelgameslib.voxelgameslib.components.inventory.events;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.components.inventory.PagedInventory;
import com.voxelgameslib.voxelgameslib.user.User;

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

    public PageChangeEvent(@Nonnull PagedInventory inventory, int oldPage, int newPage, @Nonnull ItemStack[] contents) {
        this.inventory = inventory;
        this.oldPage = oldPage;
        this.newPage = newPage;
        this.contents = contents;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    public PagedInventory getInventory() {
        return inventory;
    }

    @Nonnull
    public User getUser() {
        return inventory.getUser();
    }

    @Nonnull
    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(@Nonnull ItemStack[] contents) {
        this.contents = contents;
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
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    public int getOldPage() {
        return this.oldPage;
    }

    public int getNewPage() {
        return this.newPage;
    }
}
