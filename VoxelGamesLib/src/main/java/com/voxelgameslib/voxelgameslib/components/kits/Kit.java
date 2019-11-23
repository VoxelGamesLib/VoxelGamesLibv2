package com.voxelgameslib.voxelgameslib.components.kits;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.components.ability.Ability;
import com.voxelgameslib.voxelgameslib.util.utils.ItemBuilder;

import org.bukkit.inventory.ItemStack;

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

    public String getName() {
        return this.name;
    }

    public Map<Integer, ItemStack> getItems() {
        return this.items;
    }

    public Map<Integer, Ability> getAbilities() {
        return this.abilities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    public void setAbilities(Map<Integer, Ability> abilities) {
        this.abilities = abilities;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Kit)) return false;
        final Kit other = (Kit) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$items = this.getItems();
        final Object other$items = other.getItems();
        if (this$items == null ? other$items != null : !this$items.equals(other$items)) return false;
        final Object this$abilities = this.getAbilities();
        final Object other$abilities = other.getAbilities();
        if (this$abilities == null ? other$abilities != null : !this$abilities.equals(other$abilities)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $items = this.getItems();
        result = result * PRIME + ($items == null ? 43 : $items.hashCode());
        final Object $abilities = this.getAbilities();
        result = result * PRIME + ($abilities == null ? 43 : $abilities.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Kit;
    }

    public String toString() {
        return "Kit(name=" + this.getName() + ", items=" + this.getItems() + ", abilities=" + this.getAbilities() + ")";
    }
}
