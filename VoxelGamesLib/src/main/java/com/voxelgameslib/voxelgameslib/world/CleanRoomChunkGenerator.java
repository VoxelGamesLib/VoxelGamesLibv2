package com.voxelgameslib.voxelgameslib.world;

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

    @Nonnull
    @Override
    public byte[] generate(@Nonnull World world, @Nonnull Random random, int x, int z) {
        return new byte[32768];
    }

    @Nonnull
    @Override
    public byte[][] generateBlockSections(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biomes) {
        return new byte[world.getMaxHeight() / 16][];
    }

    @Nonnull
    @Override
    public short[][] generateExtBlockSections(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biomes) {
        return new short[world.getMaxHeight() / 16][];
    }
}
