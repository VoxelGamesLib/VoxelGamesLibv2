package com.voxelgameslib.voxelgameslib.api.game;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.voxelgameslib.voxelgameslib.api.phase.Phase;

/**
 * Holds all important information of a game, ready to be saved to and loaded from json
 */
public class GameDefinition {

    @Expose
    private GameMode gameMode;

    @Expose
    private int minPlayers;

    @Expose
    private int maxPlayers;

    @Expose
    private List<Phase> phases = new ArrayList<>();

    @Expose
    private Map<Class<GameData>, GameData> gameData = new HashMap<>();

    /**
     * @return the gamemode for this definition
     */
    @Nonnull
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * @param gameMode the gamemode for this definition
     */
    public void setGameMode(@Nonnull GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * @return the min amount of players for this game
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * @param minPlayers the min amount of players for this game
     */

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    /**
     * @return the max amount of players for this game
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @param maxPlayers the max amount of players for this game
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return the game data map
     */
    @Nonnull
    public Map<Class<GameData>, GameData> getGameData() {
        return gameData;
    }

    /**
     * @param gameData the game data map
     */
    public void setGameData(@Nonnull Map<Class<GameData>, GameData> gameData) {
        this.gameData = gameData;
    }

    /**
     * @return the phases for this game
     */
    @Nonnull
    public List<Phase> getPhases() {
        return phases;
    }

    /**
     * @param phases the phases for this game
     */
    public void setPhases(@Nonnull List<Phase> phases) {
        this.phases = phases;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameDefinition that = (GameDefinition) o;

        if (minPlayers != that.minPlayers) {
            return false;
        }
        if (maxPlayers != that.maxPlayers) {
            return false;
        }
        if (gameMode != null ? !gameMode.equals(that.gameMode) : that.gameMode != null) {
            return false;
        }
        if (phases != null ? !phases.equals(that.phases) : that.phases != null) {
            return false;
        }
        return gameData != null ? gameData.equals(that.gameData) : that.gameData == null;
    }

    @Override
    public int hashCode() {
        int result = gameMode != null ? gameMode.hashCode() : 0;
        result = 31 * result + minPlayers;
        result = 31 * result + maxPlayers;
        result = 31 * result + (phases != null ? phases.hashCode() : 0);
        result = 31 * result + (gameData != null ? gameData.hashCode() : 0);
        return result;
    }

    @Override
    @Nonnull
    public String toString() {
        return "GameDefinition{" +
                "gameMode=" + gameMode +
                ", minPlayers=" + minPlayers +
                ", maxPlayers=" + maxPlayers +
                ", phases=" + phases +
                ", gameData=" + gameData +
                '}';
    }
}
