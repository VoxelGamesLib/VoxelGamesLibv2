package com.voxelgameslib.voxelgameslib.components.signs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.user.UserHandler;
import com.voxelgameslib.voxelgameslib.utils.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

@Singleton
public class SignPlaceholders implements Listener {

    private static final Logger log = Logger.getLogger(SignPlaceholders.class.getName());
    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private UserHandler userHandler;

    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private Map<String, SignPlaceHolder> placeHolders = new HashMap<>();

    private final Map<Location, Sign> lastSeenSigns = new ConcurrentHashMap<>();

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
    public void registerPlaceholder(@Nonnull String key, @Nonnull SignPlaceHolder placeHolder) {
        placeHolders.put("[" + key + "]", placeHolder);
    }

    /**
     * gets map with all registered sign placeholders
     *
     * @return all sign placeholders
     */
    @Nonnull
    public Map<String, SignPlaceHolder> getPlaceHolders() {
        return placeHolders;
    }

    public void init() {
        registerPlaceholders();

        Bukkit.getWorlds().stream()
                .flatMap(w -> Arrays.stream(w.getLoadedChunks()))
                .flatMap(s -> Arrays.stream(s.getTileEntities()))
                .filter(s -> s instanceof Sign)
                .map(s -> (Sign) s)
                .forEach(s -> lastSeenSigns.put(s.getLocation(), s));

        // modify update packets
        protocolManager.addPacketListener(new PacketAdapter(voxelGamesLib, PacketType.Play.Server.TILE_ENTITY_DATA) {

            @Override
            public void onPacketSending(@Nonnull PacketEvent event) {
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
                    lines[i] = ComponentSerializers.JSON.deserialize(data.getString("Text" + (i + 1)));
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
                if (event.getPlayer().getLocation().distanceSquared(loc) > 200 * 200) {
                    return;
                }

                Block b = loc.getBlock();
                if (!(b.getState() instanceof Sign)) {
                    return;
                }
                Sign sign = (Sign) b.getState();
                lastSeenSigns.put(loc, sign);

                Optional<User> user = userHandler.getUser(event.getPlayer().getUniqueId());
                if (!user.isPresent()) {
                    return;
                }

                // call sign placeholders
                modifySign(user.get(), loc, rawLines, lines);

                // modify packet
                for (int i = 0; i < lines.length; i++) {
                    data.put("Text" + (i + 1), ComponentSerializers.JSON.serialize(lines[i]));
                }
            }
        });

        // update task
        new BukkitRunnable() {

            @Override
            public void run() {
                lastSeenSigns.forEach((loc, sign) -> sign.update());
            }
        }.runTaskTimer(voxelGamesLib, 20, 20);
    }

    private void modifySign(@Nonnull User user, @Nonnull Location location, @Nonnull String[] rawLines, @Nonnull Component[] lines) {
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
                                //log.info("single replace");
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
    public void handleInteract(@Nonnull PlayerInteractEvent event) {
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

    @EventHandler
    public void chunkLoad(@Nonnull ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getTileEntities())
                .filter(blockState -> blockState instanceof Sign)
                .map(blockState -> (Sign) blockState)
                .forEach(sign -> lastSeenSigns.put(sign.getLocation(), sign));
    }

    @EventHandler
    public void chunkUnload(@Nonnull ChunkUnloadEvent event) {
        Arrays.stream(event.getChunk().getTileEntities())
                .filter(blockState -> blockState instanceof Sign)
                .forEach(sign -> lastSeenSigns.remove(sign.getLocation()));
    }
}
