package me.minidigger.voxelgameslib.components.ability;

import com.google.inject.Injector;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.game.GameHandler;
import me.minidigger.voxelgameslib.tick.Tickable;
import me.minidigger.voxelgameslib.user.User;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

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
    public Ability(User user) {
        this.identifier = UUID.randomUUID();
        this.affected = user;
    }

    protected void unregister(Ability caller) {
        HandlerList.unregisterAll(caller);
        List<Game> games = injector.getInstance(GameHandler.class).getGames(affected.getUuid(), false);

        if (games.size() == 1) {
            Game game = games.get(0);

            game.getActivePhase().removeTickable(identifier);
        }
    }
}
