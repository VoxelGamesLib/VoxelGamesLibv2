package com.voxelgameslib.voxelgameslib.tick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.phase.Phase;

import org.bukkit.Bukkit;

/**
 * The TickHandler handles the ticking of all Tickables on the server. However not every Tickable is registered here.
 * {@link Phase}s and {@link Feature}s receive their ticks from the {@link Game} instance<br> Every server mod has it's
 * own implementation of the TickHandler
 */
@Singleton
public class TickHandler implements Handler {

    @Inject
    private VoxelGamesLib voxelGamesLib;

    private final List<Tickable> tickables = new ArrayList<>();
    private final List<Tickable> removeQueue = Collections.synchronizedList(new ArrayList<>());

    /**
     * Called when the underlying server mod calls a tick. Causes all {@link Tickable}s to tick
     */
    public void tick() {
        // disable old stuff
        removeQueue.forEach(Tickable::stop);
        tickables.removeAll(removeQueue);
        removeQueue.clear();

        tickables.forEach(Tickable::tick);
    }

    /**
     * Starts the ticker
     */
    @Override
    public void enable() {
        Bukkit.getServer().getScheduler().runTaskTimer(voxelGamesLib, this::tick, 1L, 1L);
    }

    /**
     * Stops every {@link Tickable} from ticking and does some cleanup
     */
    @Override
    public void disable() {
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
    public void end(@Nonnull Tickable tickable) {
        removeQueue.add(tickable);
    }
}
