package me.minidigger.voxelgameslib.game;

import javax.annotation.Nonnull;

/**
 * Custom game class with no actuall stuff in it, used to generate custom games via game
 * definitions
 */
public class CustomGame extends AbstractGame {

  /**
   * Constructs a new {@link AbstractGame}
   *
   * @param mode the mode this {@link Game} is an instance of.
   */
  public CustomGame(@Nonnull GameMode mode) {
    super(mode);
  }

  @Override
  public void initGameFromModule() {

  }
}
