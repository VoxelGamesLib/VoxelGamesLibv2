package com.voxelgameslib.voxelgameslib.stats;

import org.hibernate.annotations.Type;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.voxelgameslib.voxelgameslib.event.events.player.PlayerDecrementStatEvent;
import com.voxelgameslib.voxelgameslib.event.events.player.PlayerIncrementStatEvent;
import com.voxelgameslib.voxelgameslib.user.User;

@Entity
@Table(name = "stat")
public class StatInstance {

    @Transient
    private User user;
    @Transient
    private boolean dirty = false;

    @Id
    @Type(type = "uuid-char")
    private UUID id;
    private double val;
    @Enumerated(EnumType.STRING)
    private StatType statType;

    protected StatInstance() {
    }

    public StatInstance(User user, StatType statType, double val) {
        this.user = user;
        this.id = user.getUuid();
        this.statType = statType;
        this.val = val;
    }

    public void increment() {
        increment(1);
    }

    public void increment(double val) {
        PlayerIncrementStatEvent event = new PlayerIncrementStatEvent(getUser(), statType, this.val, this.val + val, val);
        if (event.callEvent()) {
            this.val = event.getNewVal();
            dirty = true;
        }
    }

    public void decrement() {
        decrement(1);
    }

    public void decrement(double val) {
        PlayerDecrementStatEvent event = new PlayerDecrementStatEvent(getUser(), statType, this.val, this.val - val, val);
        if (event.callEvent()) {
            this.val = event.getNewVal();
            dirty = true;
        }
    }

    public double getVal() {
        return val;
    }

    public boolean isDirty() {
        return dirty;
    }

    public User getUser() {
        if (user == null) {
            user = statType.getUser(id);
        }
        return user;
    }
}
