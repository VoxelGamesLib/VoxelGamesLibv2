package me.minidigger.voxelgameslib.game;

import java.util.List;

import me.minidigger.voxelgameslib.components.team.Team;
import me.minidigger.voxelgameslib.map.MapInfo;

/**
 * Default game data class used internally by the framework
 */
public class DefaultGameData implements GameData {

    public MapInfo lobbyMap;
    public MapInfo voteWinner;

    public List<Team> teams;
}
