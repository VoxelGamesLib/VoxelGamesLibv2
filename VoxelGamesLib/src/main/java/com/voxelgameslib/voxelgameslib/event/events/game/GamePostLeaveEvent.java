package com.voxelgameslib.voxelgameslib.event.events.game;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

/**
 * Called when a user leaves a game. Called after he left the game to coordinate further actions
 */
public class GamePostLeaveEvent extends GameEvent {

    private static final HandlerList handlers = new HandlerList();
    private User user;
    private boolean shouldTeleportToSpawn;
    private Location spawnLocation;

    /**
     * @param game the game the user left
     * @param user the user that left the game
     */
    public GamePostLeaveEvent(@Nonnull Game game, @Nonnull User user, Location spawnLocation, boolean shouldTeleportToSpawn) {
        super(game);
        this.user = user;
        this.spawnLocation = spawnLocation;
        this.shouldTeleportToSpawn = shouldTeleportToSpawn;
    }

    /**
     * @return the user that left the game
     */
    @Nonnull
    public User getUser() {
        return user;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean shouldTeleportToSpawn() {
        return shouldTeleportToSpawn;
    }

    public void setShouldTeleportToSpawn(boolean shouldTeleportToSpawn) {
        this.shouldTeleportToSpawn = shouldTeleportToSpawn;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
