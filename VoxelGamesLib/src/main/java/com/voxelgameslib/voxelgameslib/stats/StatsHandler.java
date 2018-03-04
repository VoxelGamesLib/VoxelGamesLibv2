package com.voxelgameslib.voxelgameslib.stats;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.timings.Timings;
import com.voxelgameslib.voxelgameslib.user.User;

import org.bukkit.Bukkit;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.SubclassMatchProcessor;

@Singleton
public class StatsHandler implements Handler {

    @Inject
    private VoxelGamesLib vgl;
    @Inject
    private Injector injector;

    private List<Stat> statTypes = new ArrayList<>();
    private Table<Stat, UUID, StatInstance> instances = HashBasedTable.create();

    @Override
    public void enable() {
        Bukkit.getScheduler().runTaskTimer(vgl, () -> statTypes.stream().filter(Stat::shouldTick).forEach(Stat::tickOneMinute), 20 * 60, 20 * 60);

        Timings.time("RegisterStatTypes",
            () -> new FastClasspathScanner().addClassLoader(getClass().getClassLoader())
                .matchSubclassesOf(Stat.class, (SubclassMatchProcessor<Stat>) this::registerStatType).scan());
    }

    @Override
    public void disable() {

    }

    public void registerStatType(Class<? extends Stat> clazz) {
        Stat stat = injector.getInstance(clazz);
        statTypes.add(stat);
        if (stat.getListener() != null) {
            Bukkit.getPluginManager().registerEvents(stat.getListener(), vgl);
        }
    }

    public StatInstance getInstance(Stat stat, User user) {
        StatInstance instance = instances.get(stat, user.getUuid());
        if (instance == null) {
            instance = new StatInstance(user, stat, stat.defaultValue());
            instances.put(stat, user.getUuid(), instance);
        }
        return instance;
    }
}
