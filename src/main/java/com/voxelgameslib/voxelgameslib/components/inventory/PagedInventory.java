package com.voxelgameslib.voxelgameslib.components.inventory;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.voxelgameslib.voxelgameslib.components.inventory.events.PageChangeEvent;
import com.voxelgameslib.voxelgameslib.exception.ComponentException;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A PagedInventory is a type of inventory.
 *
 * It has support for pagination, so you can have a multi-paged inventory.
 */
public class PagedInventory extends BaseInventory {

    private ItemStack forward, backward, close;
    private int currentPage;
    private boolean dynamicInventory = true;
    private String titleFormat = "%title% - Page %page%";
    private String activeTitle;
    private Map<Integer, ItemStack[]> pages = new HashMap<>();

    private boolean open;

    /**
     * Creates a new PagedInventory
     *
     * @see BaseInventory#BaseInventory(User, String, int)
     */
    public PagedInventory(@Nonnull User user, @Nonnull String title, int size) {
        //super(user, title, size);
        //init();
        this(user, title);
    }

    /**
     * Creates a new PagedInventory <p> Has the ability to specify if the inventory is dynamic (size determined by
     * contents)
     *
     * @see BaseInventory#BaseInventory(User, String, int)
     */
    public PagedInventory(@Nonnull User user, @Nonnull String title) {
        super(user, title, 54);
        this.dynamicInventory = true;
        init();
    }

    @Nonnull
    public ItemStack getForwardItem() {
        return new ItemBuilder(forward).name(ChatColor.GOLD + "Next").build();//TODO i18n
    }

    public void setForwardItem(@Nonnull ItemStack item) {
        forward = item;
    }

    @Nonnull
    public ItemStack getBackwardItem() {
        return new ItemBuilder(backward).name(ChatColor.GOLD + "Back").build();//TODO i18n;
    }

    public void setBackwardItem(@Nonnull ItemStack item) {
        backward = item;
    }

    @Nonnull
    public ItemStack getCloseItem() {
        return new ItemBuilder(close).name(ChatColor.RED + "Next").build();//TODO i18n
    }

    public void setCloseItem(@Nonnull ItemStack item) {
        close = item;
    }

    /**
     * Sets the format for the inventory title. <p> Available placeholders: %title% - replaced with inventory title
     * (defined on object creation) %page% - replaced with page number
     *
     * @param titleFormat format
     */
    public void setTitleFormat(@Nonnull String titleFormat) {
        this.titleFormat = titleFormat;
    }

    /**
     * Get a page
     *
     * @param page id of the page
     */
    @Nonnull
    public Optional<ItemStack[]> getPage(int page) {
        return Optional.ofNullable(pages.get(page));
    }

    /**
     * Create a new page, or edit an existing page.
     *
     * @param contents the contents of the page
     * @param page     the id of the page, if you are editing an existing page
     */
    public void createOrEditPage(@Nonnull ItemStack[] contents, @Nullable Integer page) {
        if (page != null && page < 0) {
            throw new ComponentException("Attempted to register/modify a page below index 0", getClass().getSimpleName());
        }

        if (contents.length > 54 - 9) {
            throw new ComponentException("Tried to have too many ItemStacks when creating a new page", getClass().getSimpleName());
        }

        if (contents.length % 9 != 0) {
            int fill = 9 - contents.length % 9;
            ItemStack[] filler = new ItemStack[fill];
            for (int i = 0; i < fill; i++) {
                filler[i] = new ItemStack(Material.AIR);
            }
            contents = (ItemStack[]) ArrayUtils.addAll(contents, filler);
        }

        // todo, condense code down, this is ugly

        ItemStack[] navigation = new ItemStack[9];
        navigation[0] = new ItemStack(Material.AIR);
        navigation[1] = new ItemStack(Material.AIR);
        navigation[2] = new ItemStack(Material.AIR);
        navigation[3] = getBackwardItem();
        navigation[4] = getCloseItem();
        navigation[5] = getForwardItem();
        navigation[6] = new ItemStack(Material.AIR);
        navigation[7] = new ItemStack(Material.AIR);
        navigation[8] = new ItemStack(Material.AIR);

        System.out.println("create page with" + contents.length + " items");
        ItemStack[] finalContents = (ItemStack[]) ArrayUtils.addAll(contents, navigation);

        if (page == null) {
            pages.put(pages.size(), finalContents);
        } else {
            pages.put(page, finalContents);
        }
    }

    /**
     * Sets the visible page of the inventory
     *
     * @param newPage id of page to set as visible
     */
    public void setPage(int newPage) {
        getPage(newPage).ifPresent(contents -> {
            PageChangeEvent pageChangeEvent = new PageChangeEvent(this, currentPage, newPage, contents);
            Bukkit.getPluginManager().callEvent(pageChangeEvent);

            if (!pageChangeEvent.isCancelled()) {
                currentPage = newPage;

                if (dynamicInventory) {
                    size = contents.length;
                }

                updateTitle();
                constructNewInventory();
                this.bukkitInventory.setContents(pageChangeEvent.getContents());

                if (open) {
                    // this looks so dumb, but trust me, this is what you call sexy code that does shit... or at least i think it does shit
                    open();
                }
            }
        });
    }

    /**
     * Automatically construct pages based off the item stack
     */
    public void autoConstructPages(@Nonnull ItemStack... items) {
        // TODO please dont judge me if none of this works. all theoretical code. could be a load of bs for all i know
        // such is the talent of a master developur

        pages.clear();

        int count = 0;
        int page = 0;
        int size = 0;
        int pagesToFill = 1;
        int sizeOfLast;

        if (items.length > 54 - 9) {
            size = 54;
            pagesToFill = (int) Math.ceil(items.length / (54 - 9));
            sizeOfLast = items.length % (54 - 9);
        } else {
            size = items.length;
            sizeOfLast = items.length;
        }

        ItemStack[] currentPageItems = new ItemStack[size];

        for (ItemStack item : items) {
            currentPageItems[count] = item;

            count++;

            if (count == 54 - 9) {
                createOrEditPage(currentPageItems, page);

                page++;

                if (page == pagesToFill) {
                    currentPageItems = new ItemStack[sizeOfLast];
                } else {
                    Arrays.fill(currentPageItems, null);
                }
            } else if (page == pagesToFill - 1 && count == sizeOfLast) {
                createOrEditPage(currentPageItems, page);
            }
        }

        setPage(0);
    }

    /**
     * @see BaseInventory#open()
     */
    public void open() {
        super.open();

        this.open = true;
    }

    /**
     * @see BaseInventory#close()
     */
    public void close() {
        super.close();

        this.open = false;
    }

    private void updateTitle() {
        activeTitle = titleFormat
                .replace("%title%", title)
                .replace("%page%", currentPage + "");
    }

    private void init() {
        forward = new ItemBuilder(Material.ARROW).build();
        backward = new ItemBuilder(Material.ARROW).build();
        close = new ItemBuilder(Material.REDSTONE).build();

        addClickAction(forward, (itemStack, clickEvent) -> {
            setPage(++currentPage);
        });

        addClickAction(backward, (itemStack, clickEvent) -> {
            setPage(--currentPage);
        });

        addClickAction(close, ((itemStack, clickEvent) -> {
            close();
        }));

        setPage(0);
        updateTitle();
        constructNewInventory();
    }
}
