package com.voxelgameslib.voxelgameslib.components.user;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

public class PlayerState {

    private ItemStack[] inventory;
    private double health;
    private int exp;
    private float saturation;

    @java.beans.ConstructorProperties({"inventory", "health", "exp", "saturation"})
    public PlayerState(ItemStack[] inventory, double health, int exp, float saturation) {
        this.inventory = inventory;
        this.health = health;
        this.exp = exp;
        this.saturation = saturation;
    }

    @Nonnull
    public static PlayerState of(@Nonnull User user) {
        return new PlayerState(
                user.getPlayer().getInventory().getContents(),
                user.getPlayer().getHealth(),
                user.getPlayer().getTotalExperience(),
                user.getPlayer().getSaturation()
        );
    }

    public void apply(@Nonnull User user) {
        user.getPlayer().getInventory().setContents(inventory);
        user.getPlayer().updateInventory();
        user.getPlayer().setHealth(health);
        user.getPlayer().setTotalExperience(exp);
        user.getPlayer().setSaturation(saturation);
    }

    public ItemStack[] getInventory() {
        return this.inventory;
    }

    public double getHealth() {
        return this.health;
    }

    public int getExp() {
        return this.exp;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerState)) return false;
        final PlayerState other = (PlayerState) o;
        if (!other.canEqual((Object) this)) return false;
        if (!java.util.Arrays.deepEquals(this.getInventory(), other.getInventory())) return false;
        if (Double.compare(this.getHealth(), other.getHealth()) != 0) return false;
        if (this.getExp() != other.getExp()) return false;
        if (Float.compare(this.getSaturation(), other.getSaturation()) != 0) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getInventory());
        final long $health = Double.doubleToLongBits(this.getHealth());
        result = result * PRIME + (int) ($health >>> 32 ^ $health);
        result = result * PRIME + this.getExp();
        result = result * PRIME + Float.floatToIntBits(this.getSaturation());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerState;
    }

    public String toString() {
        return "PlayerState(inventory=" + java.util.Arrays.deepToString(this.getInventory()) + ", health=" + this.getHealth() + ", exp=" + this.getExp() + ", saturation=" + this.getSaturation() + ")";
    }
}
