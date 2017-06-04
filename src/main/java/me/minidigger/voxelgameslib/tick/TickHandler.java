package me.minidigger.voxelgameslib.tick;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.handler.Handler;
import me.minidigger.voxelgameslib.phase.Phase;

/**
 * The TickHandler handles the ticking of all Tickables on the server. However not every Tickable is
 * registered here. {@link Phase}s and {@link Feature}s receive their ticks from the {@link me.minidigger.voxelgameslib.game.Game} instance<br> Every server mod has it's own
 * implementation of the TickHandler
 */
public abstract class TickHandler implements Handler {

  private final List<Tickable> tickables = new ArrayList<>();

  /**
   * Called when the underlying server mod calls a tick. Causes all {@link Tickable}s to tick
   */
  public void tick() {
    tickables.forEach(Tickable::tick);
  }

  /**
   * Stops every {@link Tickable} from ticking and does some cleanup
   */
  @Override
  public void stop() {
    tickables.forEach(Tickable::stop);
    tickables.clear();
  }

  /**
   * Registers a new {@link Tickable}. Calls the {@link Tickable#start()} method.
   *
   * @param tickable the new {@link Tickable} that should now receive server ticks
   */
  public void registerTickable(@Nonnull Tickable tickable) {
    tickables.add(tickable);
    tickable.start();
  }

  /**
   * Remove the tickable form the tickloop
   *
   * @param tickable the tickable which should no longer receive ticks
   */
  public void end(Tickable tickable) {
    tickables.remove(tickable);
  }
}
