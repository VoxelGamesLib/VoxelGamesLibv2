package com.voxelgameslib.voxelgameslib.event.events.player;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.stats.StatType;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerDecrementStatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private StatType statType;
    private double oldVal;
    private double newVal;
    private double decrementAmount;

    public PlayerDecrementStatEvent(@Nonnull User user, StatType statType, double oldVal, double newVal, double decrementAmount) {
        super(user);
        this.statType = statType;
        this.oldVal = oldVal;
        this.newVal = newVal;
        this.decrementAmount = decrementAmount;
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

    public StatType getStat() {
        return statType;
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

    public double getDecrementAmount() {
        return decrementAmount;
    }
}
