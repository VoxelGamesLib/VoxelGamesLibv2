package com.voxelgameslib.voxelgameslib.api.game;

import java.util.List;

import com.voxelgameslib.voxelgameslib.components.map.MapInfo;
import com.voxelgameslib.voxelgameslib.components.team.Team;

/**
 * Default game data class used internally by the framework
 */
public class DefaultGameData implements GameData {

    public MapInfo lobbyMap;
    public MapInfo voteWinner;

    public List<Team> teams;
}
