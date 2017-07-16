package me.minidigger.voxelgameslib.event;

import java.util.Optional;

import me.minidigger.voxelgameslib.user.User;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventFilter {

    boolean filter(Event event, RegisteredListener registeredListener, Optional<User> user);
}
