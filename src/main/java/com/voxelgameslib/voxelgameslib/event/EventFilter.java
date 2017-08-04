package com.voxelgameslib.voxelgameslib.event;

import com.voxelgameslib.voxelgameslib.user.User;

import java.util.Optional;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventFilter {

    boolean filter(Event event, RegisteredListener registeredListener, Optional<User> user);
}
