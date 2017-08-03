package com.voxelgameslib.voxelgameslib.event;

import java.util.Optional;

import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventFilter {

    boolean filter(Event event, RegisteredListener registeredListener, Optional<User> user);
}
