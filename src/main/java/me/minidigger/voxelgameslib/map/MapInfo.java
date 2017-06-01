package me.minidigger.voxelgameslib.map;

import com.google.gson.annotations.Expose;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Small class to handle info about this map
 */
public class MapInfo {

  @Expose
  private String name;
  @Expose
  private String author;
  @Expose
  private List<String> gamemode;

  /**
   * @param name the display name of the map
   * @param author the author of the map
   * @param gamemode the gamemodes this map supports
   */
  public MapInfo(@Nonnull String name, @Nonnull String author, @Nonnull List<String> gamemode) {
    this.name = name;
    this.author = author;
    this.gamemode = gamemode;
  }

  /**
   * @return the display name of this map
   */
  @Nonnull
  public String getName() {
    return name;
  }

  /**
   * @param name the display name to set
   */
  public void setName(@Nonnull String name) {
    this.name = name;
  }

  /**
   * @return the author of this map
   */
  @Nonnull
  public String getAuthor() {
    return author;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(@Nonnull String author) {
    this.author = author;
  }

  /**
   * @return the gamemodes this map supports
   */
  @Nonnull
  public List<String> getGamemodes() {
    return gamemode;
  }

  /**
   * @param gamemode the gamemodes to set
   */
  public void setGamemode(@Nonnull List<String> gamemode) {
    this.gamemode = gamemode;
  }

  @Override
  public boolean equals(@Nonnull Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MapInfo mapInfo = (MapInfo) o;

    if (name != null ? !name.equals(mapInfo.name) : mapInfo.name != null) {
      return false;
    }
    if (author != null ? !author.equals(mapInfo.author) : mapInfo.author != null) {
      return false;
    }
    return gamemode != null ? gamemode.equals(mapInfo.gamemode) : mapInfo.gamemode == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (author != null ? author.hashCode() : 0);
    result = 31 * result + (gamemode != null ? gamemode.hashCode() : 0);
    return result;
  }

  @Override
  @Nonnull
  public String toString() {
    return "MapInfo{" +
        "name='" + name + '\'' +
        ", author='" + author + '\'' +
        ", gamemode=" + gamemode +
        '}';
  }
}
