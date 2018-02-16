package com.voxelgameslib.voxelgameslib.components.kits;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import com.dumptruckman.bukkit.configuration.json.JsonConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@Singleton
public class KitHandler implements Handler {

    private static final Logger log = Logger.getLogger(KitHandler.class.getName());
    @Inject
    @Named("KitsFolder")
    private File kitsDir;
    @Inject
    @Named("IgnoreExposedBS")
    private Gson gson;

    private Map<String, Kit> kits = new HashMap<>();
    private Set<String> availableKits = new HashSet<>();

    @Override
    public void enable() {
        if (!kitsDir.exists()) {
            log.info("Kits dir doesn't exist, creating....");
            kitsDir.mkdirs();
        }

        File[] files = kitsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    availableKits.add(file.getName().replace(".json", ""));
                }
            }
        }
        log.info("There are " + availableKits.size() + " kits available.");

        // test stuff
        Kit kit = new Kit("DefaultKit");
        kit.addItem(0, new ItemBuilder(Material.STONE).name("Test Stone").build());
        kit.addItem(1, new ItemBuilder(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 5).name(ChatColor.RED + "Cool sword").amount(2).build());
        kit.addItem(2, new ItemBuilder(Material.LEATHER_BOOTS).enchantment(Enchantment.PROTECTION_EXPLOSIONS, 2).enchantment(Enchantment.PROTECTION_FALL, 5).name("Cool bots").amount(3).color(Color.RED).durability(10).lore("test").lore("Lore").build());
        createKit(kit);
        kit = loadKit("DefaultKit", new File(kitsDir, kit.getName() + ".json"));
        System.out.println(kit);
    }

    @Nonnull
    public Optional<Kit> loadKit(@Nonnull String name) {
        if (!availableKits.contains(name)) {
            log.warning("Tried to load unknown kit");
            return Optional.empty();
        }

        if (kits.containsKey(name)) {
            return Optional.of(kits.get(name));
        } else {
            return Optional.ofNullable(loadKit(name, new File(kitsDir, name + ".json")));
        }
    }

    public void createKit(@Nonnull Kit kit) {
        availableKits.add(kit.getName());
        kits.put(kit.getName(), kit);
        saveKit(kit, new File(kitsDir, kit.getName() + ".json"));
    }

    @Nullable
    private Kit loadKit(@Nonnull String name, @Nonnull File file) {
        try {
            JsonConfiguration jsonConfiguration = JsonConfiguration.loadConfiguration(file);
            Kit kit = new Kit();
            kit.setName(jsonConfiguration.getString("name", "INVALID"));

            Map<String, Object> map = jsonConfiguration.getConfigurationSection("items").getValues(false);
            Map<Integer, ItemStack> items = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                items.put(Integer.parseInt(entry.getKey()), (ItemStack) entry.getValue());
            }
            kit.setItems(items);

            // TODO load abilities
            kits.put(name, kit);
            return kit;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void saveKit(@Nonnull Kit kit, @Nonnull File file) {
        try {
            JsonConfiguration jsonConfiguration = new JsonConfiguration();
            jsonConfiguration.set("name", kit.getName());

            jsonConfiguration.createSection("items", kit.getItems());

            // TODO save abilities

            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(new JsonParser().parse(jsonConfiguration.saveToString()), fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {

    }
}
