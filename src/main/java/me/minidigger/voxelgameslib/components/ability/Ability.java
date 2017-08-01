package me.minidigger.voxelgameslib.components.ability;

import me.minidigger.voxelgameslib.tick.Tickable;
import me.minidigger.voxelgameslib.user.User;
import org.bukkit.event.Listener;

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
}
