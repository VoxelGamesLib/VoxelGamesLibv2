package me.minidigger.voxelgameslib.components.scoreboard;

/**
 * Represents a line of a scoreboard
 */
public interface ScoreboardLine {

    /**
     * @return the (current) value of this line
     */
    String getValue();

    /**
     * changes the value of this line
     *
     * @param value the new value
     */
    void setValue(String value);
}
