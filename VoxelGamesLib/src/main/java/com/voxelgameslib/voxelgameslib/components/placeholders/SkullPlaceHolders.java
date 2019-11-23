package com.voxelgameslib.voxelgameslib.components.placeholders;

import com.comphenix.packetwrapper.WrapperPlayServerTileEntityData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.destroystokyo.paper.profile.PlayerProfile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.api.stats.StatsHandler;
import com.voxelgameslib.voxelgameslib.api.stats.Trackable;
import com.voxelgameslib.voxelgameslib.internal.texture.TextureHandler;
import com.voxelgameslib.voxelgameslib.util.Pair;
import com.voxelgameslib.voxelgameslib.util.utils.NBTUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

@Singleton
public class SkullPlaceHolders implements Listener {

    private static final Logger log = Logger.getLogger(SkullPlaceHolder.class.getName());

    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private Map<String, SkullPlaceHolder> placeHolders = new HashMap<>();
    private final Map<Location, Skull> lastSeenSkulls = new ConcurrentHashMap<>();

    @Inject
    private TextureHandler textureHandler;
    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private StatsHandler statsHandler;

    /**
     * registers the default skull placeholders
     */
    public void registerPlaceholders() {
        registerPlaceholder("god", (name, player, location, args) ->
                textureHandler.getPlayerProfile(ThreadLocalRandom.current().nextBoolean() ? "MiniDigger" : "Notch"));

        registerPlaceholder("top", (name, player, location, args) -> {
            if (args.length < 2) {
                return null;
            }

            Optional<Trackable> type = StatsHandler.fromName(args[1]);
            if (!type.isPresent()) {
                return null;
            }

            int index = 0;
            if (args.length == 3) {
                try {
                    index = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    return null;
                }
            }

            List<Pair<UUID, Double>> list = statsHandler.getTopWithUUID(type.get(), Math.max(index, 5));
            if (list.size() < index + 1) {
                return null;
            }

            return textureHandler.getPlayerProfile(list.get(index).getFirst());
        });
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, voxelGamesLib);

        registerPlaceholders();

        // listener
        protocolManager.addPacketListener(new PacketAdapter(voxelGamesLib, PacketType.Play.Server.TILE_ENTITY_DATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerTileEntityData packet = new WrapperPlayServerTileEntityData(event.getPacket());
                event.setPacket(modifySkull(packet, event.getPlayer()));
            }
        });

        // search for already loaded skulls
        Bukkit.getWorlds().stream()
                .flatMap(w -> Arrays.stream(w.getLoadedChunks()))
                .flatMap(s -> Arrays.stream(s.getTileEntities()))
                .filter(s -> s instanceof Skull)
                .map(s -> (Skull) s)
                .forEach(s -> lastSeenSkulls.put(s.getLocation(), s));

        // update task
        new BukkitRunnable() {

            @Override
            public void run() {
                lastSeenSkulls.forEach((loc, skull) -> skull.update());
            }
        }.runTaskTimer(voxelGamesLib, 20, 20);
    }

    /**
     * registers a new placeHolder
     *
     * @param key         the key to use
     * @param placeHolder the placeholder that will replace the key
     */
    public void registerPlaceholder(@Nonnull String key, @Nonnull SkullPlaceHolder placeHolder) {
        placeHolders.put(key, placeHolder);
    }

    /**
     * gets map with all registered skull placeholders
     *
     * @return all skull placeholders
     */
    @Nonnull
    public Map<String, SkullPlaceHolder> getPlaceHolders() {
        return placeHolders;
    }

    public PacketContainer modifySkull(WrapperPlayServerTileEntityData packet, Player player) {
        NbtCompound nbt = (NbtCompound) packet.getNbtData();

        Location location = new Location(player.getWorld(), packet.getLocation().getX(), packet.getLocation().getY(), packet.getLocation().getZ());

        if (nbt.containsKey("Owner")) {
            NbtCompound owner = nbt.getCompound("Owner");
            if (owner.containsKey("Name")) {
                String name = owner.getString("Name");
                PlayerProfile profile = null;

                String[] args = name.split(":");
                SkullPlaceHolder skullPlaceHolder = placeHolders.get(args[0]);
                if (skullPlaceHolder != null) {
                    profile = skullPlaceHolder.apply(name, player, location, args);
                }

                if (profile != null && profile.hasTextures()) {
                    NBTUtil.setPlayerProfile(owner, profile);
                } else {
                    //log.warning("Error while applying placeholder '" + name + "' null? " + (profile == null) + " textures? " + (profile == null ? "" : profile.hasTextures()));
                    NBTUtil.setPlayerProfile(owner, textureHandler.getErrorProfile());
                }

                owner.setName(name);
            }

            // update last seen signs
            Block b = location.getBlock();
            if (!(b.getState() instanceof Skull)) {
                return packet.getHandle();
            }
            Skull skull = (Skull) b.getState();
            lastSeenSkulls.put(location, skull);
        }

        return packet.getHandle();
    }

    @EventHandler
    public void handleInteract(@Nonnull PlayerInteractEvent event) {
        if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Skull) {
                Skull skull = (Skull) event.getClickedBlock().getState();
                if (skull.hasMetadata("UpdateCooldown")) {
                    long cooldown = skull.getMetadata("UpdateCooldown").get(0).asLong();
                    if (cooldown > System.currentTimeMillis() - 1 * 1000) {
                        return;
                    }
                }
                skull.update();
                skull.setMetadata("UpdateCooldown", new FixedMetadataValue(voxelGamesLib, System.currentTimeMillis()));
            }
        }
    }

    @EventHandler
    public void chunkLoad(@Nonnull ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getTileEntities())
                .filter(blockState -> blockState instanceof Skull)
                .map((blockState) -> (Skull) blockState)
                .forEach(skull -> lastSeenSkulls.put(skull.getLocation(), skull));
    }

    @EventHandler
    public void chunkUnload(@Nonnull ChunkUnloadEvent event) {
        Arrays.stream(event.getChunk().getTileEntities())
                .filter(blockState -> blockState instanceof Skull)
                .forEach(sign -> lastSeenSkulls.remove(sign.getLocation()));
    }
}
