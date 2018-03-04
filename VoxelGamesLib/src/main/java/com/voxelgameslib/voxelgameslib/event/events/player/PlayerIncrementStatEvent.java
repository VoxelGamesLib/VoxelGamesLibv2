package com.voxelgameslib.voxelgameslib.event.events.player;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.stats.Stat;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerIncrementStatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private Stat stat;
    private double oldVal;
    private double newVal;
    private double incrementAmount;

    public PlayerIncrementStatEvent(@Nonnull User user, Stat stat, double oldVal, double newVal, double incrementAmount) {
        super(user);
        this.stat = stat;
        this.oldVal = oldVal;
        this.newVal = newVal;
        this.incrementAmount = incrementAmount;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public Stat getStat() {
        return stat;
    }
    public double getOldVal() {
        return oldVal;
    }

    public double getNewVal() {
        return newVal;
    }

    public void setNewVal(double newVal) {
        this.newVal = newVal;
    }

    public double getIncrementAmount() {
        return incrementAmount;
    }
}
