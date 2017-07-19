package me.minidigger.voxelgameslib.components.inventory;

/**
 * A basic inventory is a type of inventory.<br/>
 *
 * It has only one page. Clicking an item will perform an action. There will be no extra pages
 * other than the single inventory page.
 */
public class BasicInventory extends BaseInventory {
    public BasicInventory(String title, int size) {
        super(title, size);
    }
}
