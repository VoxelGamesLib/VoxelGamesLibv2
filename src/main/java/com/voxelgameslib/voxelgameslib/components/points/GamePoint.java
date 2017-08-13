package com.voxelgameslib.voxelgameslib.components.points;

import javax.persistence.Table;

import com.voxelgameslib.voxelgameslib.game.GameMode;

import lombok.AllArgsConstructor;

//@Entity
@AllArgsConstructor
@Table(name = "point_definition")
public class GamePoint implements Point {

    private String name;
    private boolean persist;

    private GameMode gameMode;

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
