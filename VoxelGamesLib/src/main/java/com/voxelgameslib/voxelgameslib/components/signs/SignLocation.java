package com.voxelgameslib.voxelgameslib.components.signs;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Stores the location of a traced sign into the db
 */
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
    public SignLocation(@Nonnull Location location, @Nonnull String[] lines) {
        this.location = location;

        block = location.getBlock();
        if (!(block.getState() instanceof Sign)) {
            throw new VoxelGameLibException("No sign at location " + location);
        }
        setLines(lines);
    }

    @Nonnull
    public String[] getLines() {
        return new String[]{lines0, lines1, lines2, lines3};
    }

    public void setLines(@Nonnull String[] lines) {
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

    public Long getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public Block getBlock() {
        return this.block;
    }

    public String getLines0() {
        return this.lines0;
    }

    public String getLines1() {
        return this.lines1;
    }

    public String getLines2() {
        return this.lines2;
    }

    public String getLines3() {
        return this.lines3;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setLines0(String lines0) {
        this.lines0 = lines0;
    }

    public void setLines1(String lines1) {
        this.lines1 = lines1;
    }

    public void setLines2(String lines2) {
        this.lines2 = lines2;
    }

    public void setLines3(String lines3) {
        this.lines3 = lines3;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SignLocation)) return false;
        final SignLocation other = (SignLocation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$location = this.getLocation();
        final Object other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals(other$location)) return false;
        final Object this$block = this.getBlock();
        final Object other$block = other.getBlock();
        if (this$block == null ? other$block != null : !this$block.equals(other$block)) return false;
        final Object this$lines0 = this.getLines0();
        final Object other$lines0 = other.getLines0();
        if (this$lines0 == null ? other$lines0 != null : !this$lines0.equals(other$lines0)) return false;
        final Object this$lines1 = this.getLines1();
        final Object other$lines1 = other.getLines1();
        if (this$lines1 == null ? other$lines1 != null : !this$lines1.equals(other$lines1)) return false;
        final Object this$lines2 = this.getLines2();
        final Object other$lines2 = other.getLines2();
        if (this$lines2 == null ? other$lines2 != null : !this$lines2.equals(other$lines2)) return false;
        final Object this$lines3 = this.getLines3();
        final Object other$lines3 = other.getLines3();
        if (this$lines3 == null ? other$lines3 != null : !this$lines3.equals(other$lines3)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $location = this.getLocation();
        result = result * PRIME + ($location == null ? 43 : $location.hashCode());
        final Object $block = this.getBlock();
        result = result * PRIME + ($block == null ? 43 : $block.hashCode());
        final Object $lines0 = this.getLines0();
        result = result * PRIME + ($lines0 == null ? 43 : $lines0.hashCode());
        final Object $lines1 = this.getLines1();
        result = result * PRIME + ($lines1 == null ? 43 : $lines1.hashCode());
        final Object $lines2 = this.getLines2();
        result = result * PRIME + ($lines2 == null ? 43 : $lines2.hashCode());
        final Object $lines3 = this.getLines3();
        result = result * PRIME + ($lines3 == null ? 43 : $lines3.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SignLocation;
    }

    public String toString() {
        return "SignLocation(id=" + this.getId() + ", location=" + this.getLocation() + ", block=" + this.getBlock() + ", lines0=" + this.getLines0() + ", lines1=" + this.getLines1() + ", lines2=" + this.getLines2() + ", lines3=" + this.getLines3() + ")";
    }
}
