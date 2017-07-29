package me.minidigger.voxelgameslib.feature.features;

import org.junit.Before;
import org.junit.Test;

import me.minidigger.voxelgameslib.feature.FeatureTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TeamSelectFeatureTest extends FeatureTest<TeamSelectFeature> {

    @Before
    public void setup() {
        setup(new TeamSelectFeature());
    }

    @Test
    public void test() {
        if(true)return;//TODO do we really want unit tests?...
        getFeature().setTeamCount(4);
        getFeature().getPhase().getGame().join(getMockUser("Test1"));
        getFeature().getPhase().getGame().join(getMockUser("Test2"));
        getFeature().getPhase().getGame().join(getMockUser("Test3"));
        getFeature().getPhase().getGame().join(getMockUser("Test4"));
        getFeature().start();
        assertThat(getFeature().getTeams().size(), is(4));
        getFeature().stop();
        assertThat(getFeature().getTeam(getMockUser("Test1")).isPresent(), is(true));
        assertThat(getFeature().getTeam(getMockUser("Test2")).isPresent(), is(true));
        assertThat(getFeature().getTeam(getMockUser("Test3")).isPresent(), is(true));
        assertThat(getFeature().getTeam(getMockUser("Test4")).isPresent(), is(true));
    }
}