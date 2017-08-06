package com.voxelgameslib.voxelgameslib.components.scoreboard;

import javax.annotation.Nonnull;

/**
 * A simple line with a string, that can be updated
 */
public abstract class StringScoreboardLine implements ScoreboardLine {

    private String value;

    /**
     * Constructs a new line
     *
     * @param value the initial value
     */
    public StringScoreboardLine(@Nonnull String value) {
        this.value = value;
    }

    @Override
    @Nonnull
    public String getValue() {
        return value;
    }

    public void setValue(@Nonnull String value) {
        this.value = value;
    }
}
