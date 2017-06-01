package me.minidigger.voxelgameslib.signs;

import com.google.gson.annotations.Expose;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import me.minidigger.voxelgameslib.block.Block;
import me.minidigger.voxelgameslib.block.metadata.SignMetaData;
import me.minidigger.voxelgameslib.event.events.sign.SignUpdateEvent;
import me.minidigger.voxelgameslib.exception.VoxelGameLibException;
import me.minidigger.voxelgameslib.map.Vector3D;
import me.minidigger.voxelgameslib.server.Server;
import me.minidigger.voxelgameslib.world.World;

/**
 * Stores the location of a traced sign into the db
 */
@Data
@Entity
public class SignLocation {

  @Id
  @GeneratedValue
  private Long id;

  @Expose
  @Column
  private Vector3D location;

  @Expose
  @Column
  private String world;

  @Transient
  private Block block;

  @Expose
  @Column
  @Setter(AccessLevel.PRIVATE)
  private String lines0;
  @Expose
  @Column
  @Setter(AccessLevel.PRIVATE)
  private String lines1;
  @Expose
  @Column
  @Setter(AccessLevel.PRIVATE)
  private String lines2;
  @Expose
  @Column
  @Setter(AccessLevel.PRIVATE)
  private String lines3;

  /**
   * Constructs a new sign location
   *
   * @param location the coordinates
   * @param world the world
   * @param server the server to get block information from
   * @param lines the lines this sign currently has
   */
  public SignLocation(Vector3D location, String world, Server server, String[] lines) {
    this.location = location;
    this.world = world;

    Optional<World> w = server.getWorld(world);
    if (!w.isPresent()) {
      throw new VoxelGameLibException("Unknown world " + world);
    }

    block = w.get().getBlockAt(location);
    if (!(block.getMetaData() instanceof SignMetaData)) {
      throw new VoxelGameLibException("No sign at world " + world + " location " + location);
    }
    setLines(lines);
  }

  protected SignLocation() {
    //JPA
    setLines(new String[]{lines0, lines1, lines2, lines3});
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
