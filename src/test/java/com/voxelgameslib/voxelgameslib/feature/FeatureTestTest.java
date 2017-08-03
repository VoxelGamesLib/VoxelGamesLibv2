/*package com.voxelgameslib.voxelgameslib.feature;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FeatureTestTest extends FeatureTest<FeatureTestTest.TestFeature> {

    @Before
    public void setup() {
        setup(new TestFeature());
    }

    @Test
    public void testMocks() {
        assertThat(getFeature(), is(notNullValue()));
        assertThat(getFeature().getPhase(), is(notNullValue()));
        assertThat(getFeature().getPhase().getGame(), is(notNullValue()));
    }

    static class TestFeature extends AbstractFeature {

        @Override
        public void init() {

        }

        @Override
        public Class[] getDependencies() {
            return new Class[0];
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void tick() {

        }
    }
}
*/