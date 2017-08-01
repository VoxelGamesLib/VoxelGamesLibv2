package me.minidigger.voxelgameslib.components.ability;

import me.minidigger.voxelgameslib.event.GameEvent;
import me.minidigger.voxelgameslib.tick.Tickable;
import me.minidigger.voxelgameslib.user.User;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class Ability implements Listener, Tickable {
    protected User affected;

    /**
     * Create a new ability
     *
     * @param user the user the ability will affect/apply to
     */
    public Ability(User user) {
        this.affected = user;
    }

    @GameEvent
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getUniqueId().equals(affected.getUuid())) {
            // todo unregister tickable on death
        }
    }
}
