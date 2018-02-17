package com.voxelgameslib.voxelgameslib.components.scoreboard;

import javax.annotation.Nonnull;

/**
 * Represents a line of a scoreboard
 */
public interface ScoreboardLine {

    /**
     * @return the (current) value of this line
     */
    @Nonnull
    String getValue();

    /**
     * changes the value of this line
     *
     * @param value the new value
     */
    void setValue(@Nonnull String value);
}
