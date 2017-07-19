package me.minidigger.voxelgameslib.components.kits;

import me.minidigger.voxelgameslib.components.ability.Ability;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class Kit {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private ItemStack[] items;
    @Getter
    @Setter
    private Ability[] abilities;

    /**
     * Create a new kit
     *
     * @param name  name of kit
     * @param items items the kit contains
     */
    public Kit(String name, ItemStack[] items) {
        this(name, items, null);
    }

    /**
     * Create a new kit
     *
     * You can use the {@link me.minidigger.voxelgameslib.utils.ItemBuilder} to build an item
     *
     * @param name      name of kit
     * @param items     items the kit contains
     * @param abilities the abilities of this kit
     */
    public Kit(String name, ItemStack[] items, Ability[] abilities) {
        this.name = name;
        this.items = items;
        this.abilities = abilities;
    }
}
