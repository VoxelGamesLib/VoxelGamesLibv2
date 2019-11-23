package com.voxelgameslib.voxelgameslib.components.editmode;

import com.voxelgameslib.voxelgameslib.api.game.AbstractGame;
import com.voxelgameslib.voxelgameslib.api.game.GameMode;
import com.voxelgameslib.voxelgameslib.api.phase.phases.DummyPhase;

public class EditModeGame extends AbstractGame {

    public static GameMode GAMEMODE = new GameMode("EditMode", EditModeGame.class);

    public EditModeGame() {
        super(GAMEMODE);
    }

    @Override
    public void initGameFromModule() {
        setMaxPlayers(10);
        setMaxPlayers(1);

        DummyPhase dummyPhase = createPhase(DummyPhase.class);
        EditModePhase phase = createPhase(EditModePhase.class);

        dummyPhase.setNextPhase(phase);

        activePhase = dummyPhase;
    }
}
