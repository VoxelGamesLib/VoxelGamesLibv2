package me.minidigger.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.event.events.game.GameLeaveEvent;
import me.minidigger.voxelgameslib.feature.AbstractFeature;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.scoreboard.Scoreboard;

import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;

/**
 * Small feature that handles stuff related to the lobby phase
 */
public class LobbyFeature extends AbstractFeature {

    private Scoreboard scoreboard;
    private boolean starting = false;
    @Expose
    private int startDelay = 120 * 20; // long start delay
    private int fastStartDelay = 15 * 20; // short start delay
    private double curr = startDelay;
    private BossBar bossBar;

    @Override
    public void start() {
        // bossbar
        bossBar = getPhase().getFeature(BossBarFeature.class).getBossBar();

        // scoreboard
        scoreboard = getPhase().getFeature(ScoreboardFeature.class).getScoreboard();

        scoreboard.createAndAddLine("lobby-line",
                getPhase().getGame().getPlayers().size() + "/" + getPhase().getGame().getMinPlayers());
        scoreboard.createAndAddLine("Waiting for players...");
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {
        if (starting) {
            curr--;
            if (curr <= 0) {
                getPhase().getGame().endPhase();
                return;
            }

            bossBar.setProgress((curr / startDelay));
        }
    }

    @Override
    public void init() {

    }

    @Override
    public Class[] getDependencies() {
        return new Class[]{ScoreboardFeature.class, BossBarFeature.class};
    }

    @EventHandler
    public void onJoin(GameJoinEvent event) {
        if (event.getGame().getUuid().equals(getPhase().getGame().getUuid())) {
            scoreboard.getLine("lobby-line").ifPresent(line -> line.setValue(
                    getPhase().getGame().getPlayers().size() + "/" + getPhase().getGame().getMinPlayers()));

            if (getPhase().getGame().getPlayers().size() >= getPhase().getGame().getMinPlayers()) {
                if(!starting) {
                    starting = true;
                    curr = startDelay;
                    //TODO also update scoreboard
                    getPhase().getGame().broadcastMessage(LangKey.GAME_STARTING);
                    bossBar.setTitle(Lang.parseLegacyFormat(Lang.string(LangKey.GAME_STARTING)));
                    bossBar.setVisible(true);
                }

                if(starting && getPhase().getGame().getPlayers().size() == getPhase().getGame().getMaxPlayers()) {
                    if(curr > fastStartDelay) {
                        curr = fastStartDelay;
                    }

                    getPhase().getGame().broadcastMessage(LangKey.GAME_STARTING_ACCELERATED);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(GameLeaveEvent event) {
        if (event.getGame().getUuid().equals(getPhase().getGame().getUuid())) {
            scoreboard.getLine("lobby-line").ifPresent(line -> line.setValue(
                    getPhase().getGame().getPlayers().size() + "/" + getPhase().getGame().getMinPlayers()));
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
}
