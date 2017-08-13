package com.voxelgameslib.voxelgameslib.feature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitRootCommand;

public abstract class AbstractFeatureCommand<T extends Feature> extends BaseCommand {

    @Getter
    @Setter
    private T feature;

    private static Field registeredCommand;

    static {
        try {
            registeredCommand = BaseCommand.class.getDeclaredField("registeredCommands");
            registeredCommand.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Nonnull
    public Map<String, BukkitRootCommand> getRegisteredCommands() {
        try {
            //noinspection unchecked
            return (Map<String, BukkitRootCommand>) registeredCommand.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
