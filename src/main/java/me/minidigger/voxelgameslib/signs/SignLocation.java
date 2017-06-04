package me.minidigger.voxelgameslib.signs;

import com.google.gson.annotations.Expose;
import java.beans.Transient;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import me.minidigger.voxelgameslib.event.events.sign.SignUpdateEvent;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.map.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Stores the location of a traced sign into the db
 */
@Data
public class SignLocation {

  private Long id;

  @Expose
  private Vector3D location;

  @Expose
  private String world;

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
   * @param location the coordinates
   * @param world the world
   * @param server the server to get block information from
   * @param lines the lines this sign currently has
   */
  public SignLocation(Vector3D location, String world, String[] lines) {
    this.location = location;
    this.world = world;

    World world = Bukkit.getWorld(world);
    if (world == null) {
      throw new VoxelGameLibException("Unknown world " + world);
    }

    block = w.get().getBlockAt(location);
    if (!(block.getMetaData() instanceof SignMetaData)) {
      throw new VoxelGameLibException("No sign at world " + world + " location " + location);
    }
    setLines(lines);
  }

  public void setLines(String[] lines) {
    lines0 = lines[0];
    lines1 = lines[1];
    lines2 = lines[2];
    lines3 = lines[3];
    if (this.block != null) {
      ((SignMetaData) this.block.getMetaData()).setLines(lines);
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
  public boolean isStillValid(Server server) {
    if (block == null) {
      Optional<World> w = server.getWorld(world);
      if (!w.isPresent()) {
        throw new VoxelGameLibException("Unknown world " + world);
      }

      block = w.get().getBlockAt(location);
    }

    return block.getMetaData() instanceof SignMetaData;
  }

  /**
   * Fires a SignUpdateEvent for the sign at this location
   *
   * @param eventHandler the event handler that should handle the event
   */
  public void fireUpdateEvent(VGLEventHandler eventHandler) {
    SignMetaData metaData = (SignMetaData) block.getMetaData();
    String[] text = metaData.getLines();
    SignUpdateEvent event = new SignUpdateEvent(world, location, text);
    eventHandler.callEvent(event);
    if (!event.isCanceled()) {
      metaData.setLines(event.getText());
    }
  }
}
