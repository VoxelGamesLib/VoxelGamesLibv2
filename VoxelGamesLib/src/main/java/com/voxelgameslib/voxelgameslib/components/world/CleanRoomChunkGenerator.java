package com.voxelgameslib.voxelgameslib.components.world;

import java.util.Random;
import javax.annotation.Nonnull;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

/**
 * Created by Martin on 07.10.2016.
 */
public class CleanRoomChunkGenerator extends ChunkGenerator {

    @Override
    @Nonnull
    public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biome) {
        return createChunkData(world);
    }
}
