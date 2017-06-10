package me.minidigger.voxelgameslib.log;

import java.util.logging.LogRecord;
import java.util.logging.Logger;
import me.minidigger.voxelgameslib.handler.Handler;

public class LoggingHandler implements Handler {

  private Logger rootLogger = Logger.getLogger("me.minidigger.voxelgameslib");

  @Override
  public void start() {
    rootLogger.addHandler(new java.util.logging.Handler() {
      @Override
      public void publish(LogRecord record) {
        record.setMessage("[VoxelGamesLib] " + record.getMessage());
      }

      @Override
      public void flush() {

      }

      @Override
      public void close() throws SecurityException {

      }
    });
  }

  @Override
  public void stop() {

  }
}
