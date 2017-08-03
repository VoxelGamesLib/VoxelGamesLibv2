package com.voxelgameslib.voxelgameslib.event;

import com.voxelgameslib.voxelgameslib.user.User;
import org.bukkit.event.Event;

import java.util.Optional;

@FunctionalInterface
public interface EventFilter {

    boolean filter(Event event, RegisteredListener registeredListener, Optional<User> user);
}
