package com.voxelgameslib.voxelgameslib.components.kits;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import com.dumptruckman.bukkit.configuration.json.JsonConfiguration;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import lombok.extern.java.Log;

@Log
@Singleton
public class KitHandler implements Handler {

    @Inject
    @Named("KitsFolder")
    private File kitsDir;
    @Inject
    @Named("IgnoreExposedBS")
    private Gson gson;

    private Map<String, Kit> kits = new HashMap<>();
    private Set<String> availableKits = new HashSet<>();

    @Override
    public void start() {
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

    public Optional<Kit> loadKit(String name) {
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

    public void createKit(Kit kit) {
        availableKits.add(kit.getName());
        kits.put(kit.getName(), kit);
        saveKit(kit, new File(kitsDir, kit.getName() + ".json"));
    }

    private Kit loadKit(String name, File file) {
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

    private void saveKit(Kit kit, File file) {
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
    public void stop() {

    }
}
