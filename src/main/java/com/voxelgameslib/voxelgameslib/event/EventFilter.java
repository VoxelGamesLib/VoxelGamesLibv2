package com.voxelgameslib.voxelgameslib.event;

import java.util.Optional;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventFilter {

    boolean filter(@Nonnull Event event, @Nonnull RegisteredListener registeredListener, @Nonnull Optional<User> user);
}
