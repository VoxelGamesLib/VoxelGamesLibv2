package com.voxelgameslib.voxelgameslib.components.scoreboard;

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
    public StringScoreboardLine(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
