package me.minidigger.voxelgameslib.feature;

import java.util.ArrayList;
import java.util.List;

import me.minidigger.voxelgameslib.game.AbstractGame;
import me.minidigger.voxelgameslib.game.Game;
import me.minidigger.voxelgameslib.phase.AbstractPhase;
import me.minidigger.voxelgameslib.phase.Phase;
import me.minidigger.voxelgameslib.user.GamePlayer;
import me.minidigger.voxelgameslib.user.User;

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
        User user = spy(new GamePlayer());
        user.setDisplayName(name);
        return user;
    }
}
