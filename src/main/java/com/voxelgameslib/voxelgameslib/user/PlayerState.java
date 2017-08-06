package com.voxelgameslib.voxelgameslib.user;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerState {

    private ItemStack[] inventory;
    private double health;
    private int exp;
    private float saturation;

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
}
