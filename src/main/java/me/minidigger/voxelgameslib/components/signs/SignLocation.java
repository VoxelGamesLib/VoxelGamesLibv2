package me.minidigger.voxelgameslib.components.signs;

import com.google.gson.annotations.Expose;

import me.minidigger.voxelgameslib.exception.VoxelGameLibException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import lombok.Data;

/**
 * Stores the location of a traced sign into the db
 */
@Data
public class SignLocation {

    private Long id;

    @Expose
    private Location location;

    private Block block;

    @Expose
    private String lines0;
    @Expose
    private String lines1;
    @Expose
    private String lines2;
    @Expose
    private String lines3;

    /**
     * Constructs a new sign location
     *
     * @param location the location
     * @param lines    the lines this sign currently has
     */
    public SignLocation(Location location, String[] lines) {
        this.location = location;

        block = location.getBlock();
        if (!(block.getState() instanceof Sign)) {
            throw new VoxelGameLibException("No sign at location " + location);
        }
        setLines(lines);
    }

    public void setLines(String[] lines) {
        lines0 = lines[0];
        lines1 = lines[1];
        lines2 = lines[2];
        lines3 = lines[3];
        if (this.block != null) {
            Sign sign = ((Sign) this.block.getState());
            sign.setLine(0, lines0);
            sign.setLine(1, lines1);
            sign.setLine(2, lines2);
            sign.setLine(3, lines3);
            sign.update();
        }
    }

    public String[] getLines() {
        return new String[]{lines0, lines1, lines2, lines3};
    }

    /**
     * Checks if the block on this location is still a sign
     *
     * @return if the block is still a sign
     */
    public boolean isStillValid() {
        if (block == null) {
            if (location.getWorld() != null) {
                block = location.getBlock();
            }
        }

        return block.getState() instanceof Sign;
    }

    /**
     * Fires a SignUpdateEvent for the sign at this location
     */
    public void fireUpdateEvent() {
        Sign sign = (Sign) block.getState();
        SignChangeEvent event = new SignChangeEvent(block, null, sign.getLines());
        Bukkit.getServer().getPluginManager().callEvent(event);
        for (int i = 0; i < event.getLines().length; i++) {
            sign.setLine(i, event.getLine(i));
        }
        sign.update();
    }
}
