package com.voxelgameslib.voxelgameslib.internal.timings;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * Small class that helps with timing stuff
 */
public class Timing implements AutoCloseable {

    private static final Logger log = Logger.getLogger(Timing.class.getName());

    private final LocalDateTime start = LocalDateTime.now();
    private final String name;

    /**
     * Times a task, will print the result with log level finer
     *
     * @param name the name of the task
     */
    public Timing(final @Nonnull String name) {
        this.name = name;
    }

    @Override
    public void close() {
        final LocalDateTime end = LocalDateTime.now();
        final Duration duration = Duration.between(this.start, end);
        final String time = LocalTime.MIDNIGHT.plus(duration)
                .format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
        log.finer("Timings: " + this.name + " took " + time);
    }
}
