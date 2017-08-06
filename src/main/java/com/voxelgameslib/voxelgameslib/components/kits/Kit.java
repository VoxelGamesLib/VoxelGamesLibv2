package com.voxelgameslib.voxelgameslib.components.kits;

import com.voxelgameslib.voxelgameslib.components.ability.Ability;
import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import lombok.Data;

@Data
public class Kit {

    private String name;
    private Map<Integer, ItemStack> items;
    private Map<Integer, Ability> abilities;

    protected Kit() {
        // jpa
    }

    /**
     * Create a new kit
     *
     * @param name name of kit
     */
    public Kit(@Nonnull String name) {
        this(name, new HashMap<>(), new HashMap<>());
    }

    /**
     * Create a new kit
     *
     * @param name  name of kit
     * @param items items the kit contains
     */
    public Kit(@Nonnull String name, @Nonnull Map<Integer, ItemStack> items) {
        this(name, items, new HashMap<>());
    }

    /**
     * Create a new kit <p> You can use the {@link ItemBuilder} to build an item
     *
     * @param name      name of kit
     * @param items     items the kit contains
     * @param abilities the abilities of this kit
     */
    public Kit(@Nonnull String name, @Nonnull Map<Integer, ItemStack> items, @Nonnull Map<Integer, Ability> abilities) {
        this.name = name;
        this.items = items;
        this.abilities = abilities;
    }

    public void addItem(int slot, @Nonnull ItemStack is) {
        items.put(slot, is);
    }

    public void addAbility(int slot, @Nonnull Ability is) {
        abilities.put(slot, is);
    }
}
