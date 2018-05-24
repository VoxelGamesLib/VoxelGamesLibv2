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

import co.aikar.timings.lib.TimingManager;
import co.aikar.timings.lib.MCTiming;

/**
 * The TickHandler handles the ticking of all Tickables on the server. However not every Tickable is registered here.
 * {@link Phase}s and {@link Feature}s receive their ticks from the {@link Game} instance<br> Every server mod has it's
 * own implementation of the TickHandler
 */
@Singleton
public class TickHandler implements Handler {

    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private TimingManager timingsManager;

    private final List<Tickable> tickables = new ArrayList<>();
    private final List<Tickable> removeQueue = Collections.synchronizedList(new ArrayList<>());

    private MCTiming tickTiming;

    /**
     * Called when the underlying server mod calls a tick. Causes all {@link Tickable}s to tick
     */
    public void tick() {
        // disable old stuff
        removeQueue.forEach(Tickable::disable);
        tickables.removeAll(removeQueue);
        removeQueue.clear();

        tickTiming.startTiming();
        for (Tickable tickable : tickables) {
            MCTiming timing = timingsManager.ofStart("Tickable: " + tickable.getClass().getSimpleName(), tickTiming);
            tickable.tick();
            timing.stopTiming();
        }
        tickTiming.stopTiming();
    }

    /**
     * Starts the ticker
     */
    @Override
    public void enable() {
        tickTiming = timingsManager.of("Tickables");
        Bukkit.getServer().getScheduler().runTaskTimer(voxelGamesLib, this::tick, 1L, 1L);
    }

    /**
     * Stops every {@link Tickable} from ticking and does some cleanup
     */
    @Override
    public void disable() {
        tickables.forEach(Tickable::disable);
        tickables.clear();
    }

    /**
     * Registers a new {@link Tickable}. Calls the {@link Tickable#enable()} method.
     *
     * @param tickable the new {@link Tickable} that should now receive server ticks
     */
    public void registerTickable(@Nonnull Tickable tickable) {
        tickables.add(tickable);
        tickable.enable();
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
