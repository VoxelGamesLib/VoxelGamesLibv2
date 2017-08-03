package com.voxelgameslib.voxelgameslib.matchmaking;

import com.voxelgameslib.voxelgameslib.game.GameMode;

import lombok.Data;

@Data
public class Queue {

    private GameMode gameMode;
    private boolean ranked;
}
