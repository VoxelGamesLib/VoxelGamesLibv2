package me.minidigger.voxelgameslib.player;

import net.md_5.bungee.api.chat.BaseComponent;

public class ConsolePlayer extends GamePlayer {

  private static final ConsolePlayer instance = new ConsolePlayer();

  public static ConsolePlayer get() {
    return instance;
  }

  public ConsolePlayer() {
    super(null);
  }

  @Override
  public void sendMessage(BaseComponent[] message) {
    //TODO send BaseComponent[] to console
  }
}