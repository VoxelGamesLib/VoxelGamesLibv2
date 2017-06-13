package me.minidigger.voxelgameslib.matchmaking;

import me.minidigger.voxelgameslib.game.GameMode;

import lombok.Data;

@Data
public class Queue {

    private GameMode gameMode;
    private boolean ranked;
}
