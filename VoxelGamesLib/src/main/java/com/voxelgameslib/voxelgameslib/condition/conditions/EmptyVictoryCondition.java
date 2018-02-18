package com.voxelgameslib.voxelgameslib.condition.conditions;

import com.voxelgameslib.voxelgameslib.condition.AbstractVictoryCondition;
import com.voxelgameslib.voxelgameslib.condition.VictoryConditionInfo;

@VictoryConditionInfo(name = "Empty", author = "MiniDigger", version = "1.0", description = "Used when no other condition is used, never completes")
public class EmptyVictoryCondition extends AbstractVictoryCondition {

    @Override
    public boolean completed() {
        return false;
    }
}
