package com.voxelgameslib.voxelgameslib.editmode;

import com.voxelgameslib.voxelgameslib.game.AbstractGame;
import com.voxelgameslib.voxelgameslib.game.GameMode;
import com.voxelgameslib.voxelgameslib.phase.phases.DummyPhase;

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
