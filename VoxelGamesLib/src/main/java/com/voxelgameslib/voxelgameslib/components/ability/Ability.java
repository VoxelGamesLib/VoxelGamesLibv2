package com.voxelgameslib.voxelgameslib.components.ability;

import com.google.inject.Injector;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.game.GameHandler;
import com.voxelgameslib.voxelgameslib.tick.Tickable;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Ability implements Listener, Tickable {

    @Inject
    private Injector injector;

    protected UUID identifier;
    protected User affected;

    /**
     * Create a new ability
     *
     * @param user the user the ability will affect/apply to
     */
    public Ability(@Nonnull User user) {
        this.identifier = UUID.randomUUID();
        this.affected = user;
    }

    public void unregister() {
        unregister(false);
    }

    public void unregister(boolean removeTickable) {
        HandlerList.unregisterAll(this);
        List<Game> games = injector.getInstance(GameHandler.class).getGames(affected.getUuid(), false);

        if (games.size() == 1) {
            Game game = games.get(0);

            if (removeTickable) {
                game.getActivePhase().removeTickable(identifier);
            }
        }
    }

    public UUID getIdentifier() {
        return this.identifier;
    }
}
