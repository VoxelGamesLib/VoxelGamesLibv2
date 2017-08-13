package com.voxelgameslib.voxelgameslib.components.inventory;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.user.User;

/**
 * A basic inventory is a type of inventory. <p> It has only one page. Clicking an item will perform
 * an action. There will be no extra pages other than the single inventory page.
 */
public class BasicInventory extends BaseInventory {
    public BasicInventory(@Nonnull User user, @Nonnull String title, int size) {
        super(user, title, size);
    }
}
