package me.minidigger.voxelgameslib.signs;

import com.google.gson.annotations.Expose;
import lombok.Data;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import org.bukkit.Location;
import org.bukkit.block.Block;

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
   * @param lines the lines this sign currently has
   */
  public SignLocation(Location location, String[] lines) {
    this.location = location;

    block = location.getBlock();
    if (!(block.getMetaData() instanceof SignMetaData)) {
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
  public boolean isStillValid() {
    if (block == null) {
      if(location.getWorld() != null){
        block = location.getBlock();
      }
    }

    return block.getMetaData() instanceof SignMetaData;
  }

  /**
   * Fires a SignUpdateEvent for the sign at this location
   */
  public void fireUpdateEvent() {
    SignMetaData metaData = (SignMetaData) block.getMetaData();
    String[] text = metaData.getLines();
    SignUpdateEvent event = new SignUpdateEvent(world, location, text);
    eventHandler.callEvent(event);
    if (!event.isCanceled()) {
      metaData.setLines(event.getText());
    }
  }
}
