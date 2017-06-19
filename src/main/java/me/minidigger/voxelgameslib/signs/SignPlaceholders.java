package me.minidigger.voxelgameslib.signs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.VoxelGamesLib;

import org.bukkit.Location;

import lombok.extern.java.Log;

@Log
@Singleton
public class SignPlaceholders {

    @Inject
    private VoxelGamesLib voxelGamesLib;

    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private Map<String, SignPlaceHolder> placeHolders = new HashMap<>();

    /**
     * registers the default sign placeholders
     */
    public void registerPlaceholders() {
        registerPlaceholder("world",
                (SimpleSignPlaceHolder) (event, key) -> event.getBlock().getLocation().getWorld().getName());
        registerPlaceholder("time",
                (SimpleSignPlaceHolder) (event, key) -> DateTimeFormatter.ISO_TIME.format(LocalTime.now()));
        registerPlaceholder("location", (SimpleSignPlaceHolder) (event, key) -> {
            Location loc = event.getBlock().getLocation();
            return "X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ();
        });
    }

    /**
     * registers a new placeHolder
     *
     * @param key         the key to use
     * @param placeHolder the placeholder that will replace the key
     */
    public void registerPlaceholder(String key, SignPlaceHolder placeHolder) {
        placeHolders.put(key, placeHolder);
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
                // set sign text action
                if (action == 9) {
                    NbtBase<?> data = event.getPacket().getNbtModifier().read(0);
                    log.info("got update event with value " + data.getValue());
                }
            }

            @Override
            public void onPacketReceiving(PacketEvent event) {
            }
        });
    }

    public void update() {

    }
}
