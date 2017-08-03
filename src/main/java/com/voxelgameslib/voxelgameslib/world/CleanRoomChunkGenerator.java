package com.voxelgameslib.voxelgameslib.world;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by Martin on 07.10.2016.
 */
public class CleanRoomChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return createChunkData(world);
    }

    @Nonnull
    @Override
    public byte[] generate(World world, Random random, int x, int z) {
        return new byte[32768];
    }

    @Nonnull
    @Override
    public byte[][] generateBlockSections(@Nonnull World world, Random random, int x, int z, BiomeGrid biomes) {
        return new byte[world.getMaxHeight() / 16][];
    }

    @Nonnull
    @Override
    public short[][] generateExtBlockSections(@Nonnull World world, Random random, int x, int z, BiomeGrid biomes) {
        return new short[world.getMaxHeight() / 16][];
    }
}
