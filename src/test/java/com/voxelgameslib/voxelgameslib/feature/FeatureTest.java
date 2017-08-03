package com.voxelgameslib.voxelgameslib.feature;

import java.util.ArrayList;
import java.util.List;

import com.voxelgameslib.voxelgameslib.game.AbstractGame;
import com.voxelgameslib.voxelgameslib.phase.AbstractPhase;
import com.voxelgameslib.voxelgameslib.user.GamePlayer;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.game.Game;
import com.voxelgameslib.voxelgameslib.phase.Phase;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class FeatureTest<F extends Feature> {

    private F feature;
    private Phase phase;
    private Game game;

    private List<User> users = new ArrayList<>();

    public void setup(F f) {
        this.feature = spy(f);
        this.phase = spy(new AbstractPhase() {
        });
        this.game = spy(new AbstractGame() {
            @Override
            public void initGameFromModule() {

            }
        });

        doReturn(game).when(phase).getGame();
        doReturn(phase).when(game).getActivePhase();
        doReturn(phase).when(feature).getPhase();
        doAnswer(invocation -> users.add((User) invocation.getArguments()[0])).when(game).join(any());
    }

    public F getFeature() {
        return feature;
    }

    public User getMockUser(String name) {
        User user = Mockito.spy(new GamePlayer());
        user.setDisplayName(name);
        return user;
    }
}
