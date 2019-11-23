package com.voxelgameslib.survivalgames;

import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.AbstractGame;
import com.voxelgameslib.voxelgameslib.api.game.GameDefinition;
import com.voxelgameslib.voxelgameslib.api.game.GameInfo;
import com.voxelgameslib.voxelgameslib.api.phase.phases.GracePhase;
import com.voxelgameslib.voxelgameslib.api.phase.phases.LobbyWithVotePhase;

@GameInfo(name = "SurvivalGames", author = "MiniDigger", version = "v1.0", description = "Hungergames inspired gamemode")
public class SurvivalGamesGame extends AbstractGame {

    public SurvivalGamesGame() {
        super(SurvivalGamesPlugin.GAMEMODE);
    }

    @Override
    public void initGameFromModule() {
        setMinPlayers(2);
        setMaxPlayers(100);

        LobbyWithVotePhase lobbyWithVotePhase = createPhase(LobbyWithVotePhase.class);

        GracePhase gracePhase = createPhase(GracePhase.class);
        gracePhase.setTicks(10 * 20);
        gracePhase.addFeature(createFeature(PodFeature.class, gracePhase));

        SurvivalGamesPhase phase = createPhase(SurvivalGamesPhase.class);

        lobbyWithVotePhase.setNextPhase(gracePhase);
        gracePhase.setNextPhase(phase);

        activePhase = lobbyWithVotePhase;

        loadMap();
    }

    @Override
    public void initGameFromDefinition(@Nonnull GameDefinition gameDefinition) {
        super.initGameFromDefinition(gameDefinition);

        loadMap();
    }
}
