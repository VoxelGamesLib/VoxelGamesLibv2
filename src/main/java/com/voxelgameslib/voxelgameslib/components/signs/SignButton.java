package com.voxelgameslib.voxelgameslib.components.signs;

import com.voxelgameslib.voxelgameslib.user.User;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;

/**
 * Represents a sign a user can press to cause a action
 */
public interface SignButton {

    /**
     * Executes the action associated with this button
     *
     * @param user  the user that pressed this sign
     * @param block the block of this sign
     */
    void execute(@Nonnull User user, @Nonnull Block block);
}
