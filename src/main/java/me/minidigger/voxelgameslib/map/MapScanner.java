package me.minidigger.voxelgameslib.map;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.extern.java.Log;

/**
 * Scans the map for markers.
 */
@Log
public class MapScanner {

  /**
   * Scans a map for markers
   *
   * @param map the map to scan
   */
  public void scan(@Nonnull Map map) {
    searchForMarkers(map, map.getCenter(), map.getRadius());

    List<Marker> errored = new ArrayList<>();

    map.getMarkers().stream().filter(marker -> marker.getData().startsWith("chest:"))
        .forEach(marker -> {
          String name = marker.getData().replace("chest:", "");
          if (!map.getChestMarker(name).isPresent()) {
            log.warning(
                "Could not find a chest " + name + " for marker at " + marker.getLoc().toString());
            errored.add(marker);
          }
        });

    map.getMarkers().removeAll(errored);

    List<ChestMarker> errored2 = new ArrayList<>();

    map.getChestMarkers().stream().filter(marker -> marker.getData().startsWith("container.chest"))
        .forEach(marker -> {
          log.warning("Found unnamed chest at " + marker.getLoc().toString());
          errored2.add(marker);
        });

    map.getChestMarkers().removeAll(errored2);
  }

  /**
   * Searches the map for "markers". Most of the time these are implemented as tile entities
   * (skulls)
   *
   * @param map the map to scan
   * @param center the center location
   * @param range the range in where to scan
   */
  public void searchForMarkers(@Nonnull Map map, @Nonnull Vector3D center, int range) {

  }
}
