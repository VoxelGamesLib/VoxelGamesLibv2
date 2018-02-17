package com.voxelgameslib.voxelgameslib.game;

import java.util.List;

import com.voxelgameslib.voxelgameslib.components.team.Team;
import com.voxelgameslib.voxelgameslib.map.MapInfo;

/**
 * Default game data class used internally by the framework
 */
public class DefaultGameData implements GameData {

    public MapInfo lobbyMap;
    public MapInfo voteWinner;

    public List<Team> teams;
}
