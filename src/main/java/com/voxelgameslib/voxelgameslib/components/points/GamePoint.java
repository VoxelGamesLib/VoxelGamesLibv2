package com.voxelgameslib.voxelgameslib.components.points;

import javax.persistence.Table;

import com.voxelgameslib.voxelgameslib.game.GameMode;

//@Entity
@Table(name = "point_definition")
public class GamePoint implements Point {

    private String name;
    private boolean persist;

    private GameMode gameMode;

    @java.beans.ConstructorProperties({"name", "persist", "gameMode"})
    public GamePoint(String name, boolean persist, GameMode gameMode) {
        this.name = name;
        this.persist = persist;
        this.gameMode = gameMode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isPersist() {
        return persist;
    }

    @Override
    public void setPersist(boolean persist) {
        this.persist = persist;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
