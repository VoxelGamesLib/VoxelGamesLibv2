package com.voxelgameslib.voxelgameslib.stats;

import com.voxelgameslib.voxelgameslib.event.events.player.PlayerDecrementStatEvent;
import com.voxelgameslib.voxelgameslib.event.events.player.PlayerIncrementStatEvent;
import com.voxelgameslib.voxelgameslib.user.User;

public class StatInstance {

    private User user;
    private Stat stat;
    private double val;

    public StatInstance(User user, Stat stat, double val) {
        this.user = user;
        this.stat = stat;
        this.val = val;
    }

    public void increment() {
        increment(1);
    }

    public void increment(double val) {
        PlayerIncrementStatEvent event = new PlayerIncrementStatEvent(user, stat, this.val, this.val + val, val);
        if (event.callEvent()) {
            this.val = event.getNewVal();
        }
    }

    public void decrement() {
        decrement(1);
    }

    public void decrement(double val) {
        PlayerDecrementStatEvent event = new PlayerDecrementStatEvent(user, stat, this.val, this.val - val, val);
        if (event.callEvent()) {
            this.val = event.getNewVal();
        }
    }

    public double getVal() {
        return val;
    }
}
