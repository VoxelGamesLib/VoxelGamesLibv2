package me.minidigger.voxelgameslib.signs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.VoxelGamesLib;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;
import me.minidigger.voxelgameslib.utils.ChatUtil;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.extern.java.Log;

@Log
@Singleton
public class SignPlaceholders implements Listener {

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private UserHandler userHandler;

    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private Map<String, SignPlaceHolder> placeHolders = new HashMap<>();

    private List<Sign> lastSeenSigns = new ArrayList<>();

    /**
     * registers the default sign placeholders
     */
    public void registerPlaceholders() {
        registerPlaceholder("world", (SimpleSignPlaceHolder)
                (user, location, rawLines, lines, key) ->
                        TextComponent.of(location.getWorld().getName()));
        registerPlaceholder("time", (SimpleSignPlaceHolder)
                (user, location, rawLines, lines, key) ->
                        TextComponent.of(DateTimeFormatter.ISO_TIME.format(LocalTime.now())));
        registerPlaceholder("location", (SimpleSignPlaceHolder)
                (user, loc, rawLines, lines, key) ->
                        TextComponent.of("X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ()));
        registerPlaceholder("greeting", (FullSignPlaceHolder)
                (user, loc, rawLines, lines, key) -> new Component[]{
                        TextComponent.of("Hey there"),
                        user.getDisplayName(),
                        TextComponent.of(""),
                        TextComponent.of("")
                });
    }

    /**
     * registers a new placeHolder
     *
     * @param key         the key to use
     * @param placeHolder the placeholder that will replace the key
     */
    public void registerPlaceholder(String key, SignPlaceHolder placeHolder) {
        placeHolders.put("[" + key + "]", placeHolder);
    }

    /**
     * gets map with all registered sign placeholders
     *
     * @return all sign placeholders
     */
    public Map<String, SignPlaceHolder> getPlaceHolders() {
        return placeHolders;
    }

    public void init() {
        registerPlaceholders();

        protocolManager.addPacketListener(new PacketAdapter(voxelGamesLib, PacketType.Play.Server.TILE_ENTITY_DATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                int action = event.getPacket().getIntegers().read(0);
                // 9 = set sign text action
                if (action != 9) {
                    return;
                }

                NbtCompound data = (NbtCompound) event.getPacket().getNbtModifier().read(0);
                // read data
                Component[] lines = new Component[4];
                String[] rawLines = new String[4];
                for (int i = 0; i < lines.length; i++) {
                    lines[i] = ComponentSerializer.deserialize(data.getString("Text" + (i + 1)));
                    rawLines[i] = ChatUtil.toPlainText(lines[i]);
                }

                // sometimes its a double, sometimes its an int...
                double x;
                double y;
                double z;
                try {
                    x = data.getDouble("x");
                    y = data.getDouble("y");
                    z = data.getDouble("z");
                } catch (ClassCastException ex) {
                    x = data.getInteger("x");
                    y = data.getInteger("y");
                    z = data.getInteger("z");
                }

                Location loc = new Location(event.getPlayer().getWorld(), x, y, z);

                Block block = loc.getBlock();
                if (block.getState() instanceof Sign) {
                    Sign sign = (Sign) block.getState();
                    if (!block.hasMetadata("LastSeen")) {
                        System.out.println("add new sign");
                        lastSeenSigns.add(sign);//TODO for some reason we always add a new one here
                    }
                    block.setMetadata("LastSeen", new FixedMetadataValue(voxelGamesLib, System.currentTimeMillis()));
                } else {
                    log.severe("wat");
                    return;
                }

                Optional<User> user = userHandler.getUser(event.getPlayer().getUniqueId());
                if (!user.isPresent()) {
                    return;
                }

                // call sign placeholders
                modifySign(user.get(), loc, rawLines, lines);

                // modify packet
                for (int i = 0; i < lines.length; i++) {
                    data.put("Text" + (i + 1), ComponentSerializer.serialize(lines[i]));
                }
            }

            @Override
            public void onPacketReceiving(PacketEvent event) {
            }
        });

        // update task
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("update");
                List<Sign> toRemove = new ArrayList<>();
                lastSeenSigns.forEach(sign -> {
                    if (sign.getBlock().hasMetadata("LastSeen")) {
                        long lastSeen = sign.getBlock().getMetadata("LastSeen").get(0).asLong();
                        if (lastSeen > System.currentTimeMillis() - 10 * 60 * 1000) {
                            // mark for removal
                            toRemove.add(sign);
                            System.out.println("mark for removal");
                            sign.getBlock().removeMetadata("LastSeen", voxelGamesLib);
                        }
                    }
                    System.out.println("real update");
                    sign.update();
                });

                // give packets a bit to send out
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        System.out.println("check remove");
                        toRemove.forEach(sign -> {
                            System.out.println("check real remove");
                            // check if they now have been seen
                            if (!sign.getBlock().hasMetadata("LastSeen")) {
                                // bye
                                System.out.println("remove");
                                lastSeenSigns.remove(sign);
                            }
                        });
                    }
                }.runTaskLater(voxelGamesLib, 15);
            }
        }.runTaskTimer(voxelGamesLib, 20, 20);
    }

    private void modifySign(User user, Location location, String[] rawLines, Component[] lines) {
        for (Map.Entry<String, SignPlaceHolder> entry : placeHolders.entrySet()) {
            for (int i = 0; i < lines.length; i++) {
                String line = ChatUtil.toPlainText(lines[i]);
                if (line.contains(entry.getKey())) {
                    if (entry.getValue() instanceof FullSignPlaceHolder) {
                        FullSignPlaceHolder placeHolder = (FullSignPlaceHolder) entry.getValue();
                        Component[] replacement = placeHolder.apply(user, location, rawLines, lines, entry.getKey());
                        // apply
                        System.arraycopy(replacement, 0, lines, 0, lines.length);
                        break;// only one full sign changer
                    } else if (entry.getValue() instanceof SimpleSignPlaceHolder) {
                        SimpleSignPlaceHolder placeHolder = (SimpleSignPlaceHolder) entry.getValue();
                        Component replacement = placeHolder.apply(user, location, rawLines, lines, entry.getKey());
                        Component origLine = lines[i];
                        // apply
                        if (origLine instanceof TextComponent) {
                            TextComponent comp = (TextComponent) origLine;
                            if (comp.content().replace("[" + entry.getKey() + "]", "").length() > 0) {
                                // TODO we need to split stuff...
                                log.info("we need to split stuff...");
                            }// only that one thing, just replace stuff
                            else {
                                lines[i] = replacement;
                                log.info("single replace");
                            }
                        }// TODO need to check childs and stuff...
                        else {
                            log.info("we need to check childs and stuff...");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.hasMetadata("UpdateCooldown")) {
                    long cooldown = sign.getMetadata("UpdateCooldown").get(0).asLong();
                    if (cooldown > System.currentTimeMillis() - 1 * 1000) {
                        return;
                    }
                }
                sign.update();
                sign.setMetadata("UpdateCooldown", new FixedMetadataValue(voxelGamesLib, System.currentTimeMillis()));
            }
        }
    }
}
