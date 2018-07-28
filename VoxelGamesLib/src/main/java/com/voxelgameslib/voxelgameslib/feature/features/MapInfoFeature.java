package com.voxelgameslib.voxelgameslib.feature.features;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.components.scoreboard.Scoreboard;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.map.Map;

import org.bukkit.ChatColor;

@FeatureInfo(name = "MapInfoFeature", author = "MiniDigger", version = "1.0",
        description = "Displays some information about the current map in the scoreboard of the phase")
public class MapInfoFeature extends AbstractFeature {

    @Override
    public void enable() {
        MapFeature mapFeature = getPhase().getFeature(MapFeature.class);
        Map map = mapFeature.getMap();

        ScoreboardFeature scoreboardFeature = getPhase().getFeature(ScoreboardFeature.class);
        Scoreboard scoreboard = scoreboardFeature.getScoreboard();

        for (String mode : map.getInfo().getGamemodes()) {
            scoreboard.createAndAddLine(mode);
        }

        scoreboard.createAndAddLine(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gamemodes: ");
        scoreboard.createAndAddLine(map.getInfo().getAuthor());
        scoreboard.createAndAddLine(ChatColor.YELLOW + "" + ChatColor.BOLD + "Author: ");
        scoreboard.createAndAddLine(map.getInfo().getDisplayName());
        scoreboard.createAndAddLine(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map: ");
    }

    @Override
    @Nonnull
    public List<Class<? extends Feature>> getDependencies() {
        return Arrays.asList(MapFeature.class, ScoreboardFeature.class);
    }
}
