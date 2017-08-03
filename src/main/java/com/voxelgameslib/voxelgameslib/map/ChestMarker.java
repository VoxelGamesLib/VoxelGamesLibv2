package com.voxelgameslib.voxelgameslib.map;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A chest marker is a marker that is a chest (uh)<br> it is used to save kits or layout for chest
 * locations in the map
 */
public class ChestMarker extends Marker {

    @Nonnull
    @Expose
    private final ItemStack[] items;

    /**
     * constructs a new chest marker
     *
     * @param loc   the location that his marker is at in the world
     * @param name  the name of this chest marker (the name of the inventory)
     * @param items the items that ar in that chest
     */
    public ChestMarker(@Nonnull Vector3D loc, @Nonnull String name, @Nonnull ItemStack[] items) {
        super(loc, 0.0, name);
        this.items = items;
    }

    /**
     * @return the items that are in this chest
     */
    @Nonnull
    public ItemStack[] getItems() {
        return items;
    }
}
