package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.components.scoreboard.Scoreboard;
import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameLeaveEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;

import org.bukkit.boss.BossBar;

/**
 * Small feature that handles stuff related to the lobby phase
 */
public class LobbyFeature extends AbstractFeature {

    private static final Logger log = Logger.getLogger(LobbyFeature.class.getName());
    private Scoreboard scoreboard;
    private boolean starting = false;
    @Expose
    private int startDelay = 120 * 20; // long enable delay
    @Expose
    private int fastStartDelay = 15 * 20; // short enable delay
    private double curr;
    private BossBar bossBar;

    public LobbyFeature() {
        log.finer("creating lobby feature with starting " + starting + " curr " + curr);
    }

    @Override
    public void start() {
        curr = startDelay;
        log.finer("Starting lobby feature with starting " + starting + " curr " + curr);
        // bossbar
        bossBar = getPhase().getFeature(BossBarFeature.class).getBossBar();
        bossBar.setVisible(false);

        // scoreboard
        scoreboard = getPhase().getFeature(ScoreboardFeature.class).getScoreboard();

        scoreboard.createAndAddLine("lobby-line",
                getPhase().getGame().getPlayers().size() + "/" + getPhase().getGame().getMaxPlayers());
        scoreboard.createAndAddLine("Waiting for players...");
    }

    @Override
    public void tick() {
        if (starting) {
            curr--;
            if (curr <= 0) {
                log.finer("Timer over, ending phase");
                getPhase().getGame().endPhase();
                return;
            }

            bossBar.setProgress((curr / startDelay));
        }
    }

    @Override
    @Nonnull
    public List<Class<? extends Feature>> getDependencies() {
        return Arrays.asList(ScoreboardFeature.class, BossBarFeature.class);
    }

    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent event) {
        scoreboard.getLine("lobby-line").ifPresent(line -> line.setValue(
                getPhase().getGame().getPlayers().size() + "/" + getPhase().getGame().getMaxPlayers()));

        if (getPhase().getGame().getPlayers().size() >= getPhase().getGame().getMinPlayers()) {
            if (!starting) {
                starting = true;
                curr = startDelay;
                //TODO also update scoreboard
                getPhase().getGame().broadcastMessage(LangKey.GAME_STARTING);
                bossBar.setTitle(Lang.parseLegacyFormat(Lang.string(LangKey.GAME_STARTING)));
                bossBar.setVisible(true);
            }

            if (starting && getPhase().getGame().getPlayers().size() == getPhase().getGame().getMaxPlayers()) {
                if (curr > fastStartDelay) {
                    curr = fastStartDelay;
                }

                getPhase().getGame().broadcastMessage(LangKey.GAME_STARTING_ACCELERATED);
            }
        }
    }

    @GameEvent
    public void onLeave(@Nonnull GameLeaveEvent event) {
        scoreboard.getLine("lobby-line").ifPresent(line -> line.setValue(
                getPhase().getGame().getPlayers().size() + "/" + getPhase().getGame().getMaxPlayers()));
        if (getPhase().getGame().getPlayers().size() <= getPhase().getGame().getMinPlayers()
                && starting) {
            starting = false;
            // TODO also update scoreboard
            getPhase().getGame().broadcastMessage(LangKey.GAME_START_ABORTED);
            bossBar.setTitle("");
            bossBar.setVisible(false);
        }
    }
}
