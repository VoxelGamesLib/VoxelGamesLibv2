package com.voxelgameslib.voxelgameslib.components.kits;

import java.util.HashMap;
import java.util.Map;

import com.voxelgameslib.voxelgameslib.components.ability.Ability;

import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;
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
    public Kit(String name) {
        this(name, new HashMap<>(), new HashMap<>());
    }

    /**
     * Create a new kit
     *
     * @param name  name of kit
     * @param items items the kit contains
     */
    public Kit(String name, Map<Integer, ItemStack> items) {
        this(name, items, new HashMap<>());
    }

    /**
     * Create a new kit
     *
     * You can use the {@link ItemBuilder} to build an item
     *
     * @param name      name of kit
     * @param items     items the kit contains
     * @param abilities the abilities of this kit
     */
    public Kit(String name, Map<Integer, ItemStack> items, Map<Integer, Ability> abilities) {
        this.name = name;
        this.items = items;
        this.abilities = abilities;
    }

    public void addItem(int slot, ItemStack is) {
        items.put(slot, is);
    }

    public void addAbility(int slot, Ability is) {
        abilities.put(slot, is);
    }
}
