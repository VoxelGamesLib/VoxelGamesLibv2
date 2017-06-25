package me.minidigger.voxelgameslib.game;

import com.google.inject.Injector;

import net.kyori.text.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.chat.ChatChannel;
import me.minidigger.voxelgameslib.elo.EloHandler;
import me.minidigger.voxelgameslib.event.events.game.GameEndEvent;
import me.minidigger.voxelgameslib.event.events.game.GameJoinEvent;
import me.minidigger.voxelgameslib.event.events.game.GameLeaveEvent;
import me.minidigger.voxelgameslib.exception.NoSuchFeatureException;
import me.minidigger.voxelgameslib.feature.Feature;
import me.minidigger.voxelgameslib.feature.features.DuelFeature;
import me.minidigger.voxelgameslib.feature.features.TeamFeature;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.map.MapInfo;
import me.minidigger.voxelgameslib.phase.Phase;
import me.minidigger.voxelgameslib.team.Team;
import me.minidigger.voxelgameslib.tick.TickHandler;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.utils.ChatUtil;
import me.minidigger.voxelgameslib.world.WorldHandler;

import org.bukkit.Bukkit;

import lombok.extern.java.Log;

/**
 * Abstract implementation of a {@link Game}. Handles broadcasting, ticking and user management.
 */
@Log
public abstract class AbstractGame implements Game {

    @Inject
    private Injector injector;
    @Inject
    private TickHandler tickHandler;
    @Inject
    private GameHandler gameHandler;
    @Inject
    private EloHandler eloHandler;
    @Inject
    private WorldHandler worldHandler;

    @Nonnull
    private final GameMode gameMode;
    protected Phase activePhase;

    private UUID uuid;

    private int minPlayers;
    private int maxPlayers;

    private final List<User> players = new ArrayList<>();
    private final List<User> spectators = new ArrayList<>();

    private Map<String, Object> gameData = new HashMap<>();

    private boolean aborted = false;

    private LocalDateTime startTime;
    private Duration duration;

    private ChatChannel chatChannel;

    /**
     * Constructs a new {@link AbstractGame}
     *
     * @param mode the mode this {@link Game} is an instance of.
     */
    public AbstractGame(@Nonnull GameMode mode) {
        this.gameMode = mode;
    }

    @Override
    public void setUuid(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void broadcastMessage(@Nonnull Component message) {
        players.forEach(u -> u.sendMessage(message));
        spectators.forEach(u -> u.sendMessage(message));
    }

    @Override
    public void broadcastMessage(@Nonnull LangKey key, @Nullable Object... args) {
        players.forEach(user -> Lang.msg(user, key, args));
        spectators.forEach(user -> Lang.msg(user, key, args));
    }

    @Override
    public void start() {
        startTime = LocalDateTime.now();
        chatChannel = new ChatChannel("game." + getUuid().toString());

        activePhase.setRunning(true);
        activePhase.start();
    }

    /**
     * @deprecated this method does nothing, use endGame instead ;)
     */
    @Deprecated
    @Override
    public void stop() {
        // ignore stop from tick handler, we only need to care about that stop if server shuts down
        // and then we know about it via the game handler and endGame() anyways
    }

    @Override
    public void initGameFromDefinition(@Nonnull GameDefinition gameDefinition) {
        setMaxPlayers(gameDefinition.getMaxPlayers());
        setMinPlayers(gameDefinition.getMinPlayers());
        activePhase = gameDefinition.getPhases().get(0);
        gameData = gameDefinition.getGameData();

        // fix stuff
        for (int i = 0; i < gameDefinition.getPhases().size(); i++) {
            Phase nextPhase = null;
            if (gameDefinition.getPhases().size() > i + 1) {
                nextPhase = gameDefinition.getPhases().get(i + 1);
            }
            Phase currPhase = gameDefinition.getPhases().get(i);
            currPhase.setNextPhase(nextPhase);
            currPhase.setGame(this);

            for (Feature feature : currPhase.getFeatures()) {
                feature.setPhase(currPhase);
            }

            for (Feature feature : currPhase.getFeatures()) {
                feature.init();
            }
        }
    }

    @Override
    @Nonnull
    public GameDefinition saveGameDefinition() {
        GameDefinition definition = new GameDefinition();
        definition.setGameMode(getGameMode());
        definition.setMaxPlayers(getMaxPlayers());
        definition.setMinPlayers(getMinPlayers());
        List<Phase> phases = new ArrayList<>();
        Phase phase = activePhase;
        while (phase != null) {
            phases.add(phase);
            phase = phase.getNextPhase();
        }
        definition.setPhases(phases);
        definition.setGameData(gameData);

        return definition;
    }

    @Override
    public void tick() {
        activePhase.tick();
    }

    @Override
    public void endPhase() {
        activePhase.setRunning(false);
        activePhase.stop();
        if (activePhase.getNextPhase() != null) {
            activePhase = activePhase.getNextPhase();
            assert activePhase != null;
            activePhase.setRunning(true);
            activePhase.start();
        } else {
            log.warning("Game finished without a winner?!");
            abortGame();
        }
    }

    @Override
    public void endGame(@Nullable Team winnerTeam, @Nullable User winnerUser) {
        // stop timer
        duration = Duration.between(startTime, LocalDateTime.now());

        log.finer("end game");

        handleElo(winnerTeam, winnerUser);
        //TODO handle stats
        if (winnerTeam != null) {
            Bukkit.getPluginManager()
                    .callEvent(new GameEndEvent(this, winnerTeam.getPlayers(), duration, aborted));
        } else if (winnerUser != null) {
            List<User> winningUsers = new ArrayList<User>();
            winningUsers.add(winnerUser);

            Bukkit.getPluginManager().callEvent(new GameEndEvent(this, winningUsers, duration, aborted));
        } else {
            Bukkit.getPluginManager()
                    .callEvent(new GameEndEvent(this, new ArrayList<>(), duration, aborted));
        }

        broadcastMessage(LangKey.GAME_END);

        end();
    }

    private void end() {
        while (players.size() != 0) {
            leave(players.get(0));
        }
        while (spectators.size() != 0) {
            leave(spectators.get(0));
        }

        activePhase.setRunning(false);
        activePhase.stop();

        chatChannel = null;
        tickHandler.end(this);
        gameHandler.removeGame(this);
    }

    @Override
    public void abortGame() {
        log.finer("abort  game");

        aborted = true;
        broadcastMessage(LangKey.GAME_ABORT);

        end();
    }

    private void handleElo(@Nullable Team winnerTeam, @Nullable User winnerUser) {
        boolean handled = false;
        if (winnerTeam != null) {
            try {
                TeamFeature teamFeature = getActivePhase().getFeature(TeamFeature.class);
                eloHandler.handleGameEnd(this, teamFeature);
                handled = true;
            } catch (NoSuchFeatureException ignored) {
            }
        }

        if (!handled && winnerUser != null) {
            try {
                DuelFeature duelFeature = getActivePhase().getFeature(DuelFeature.class);
                eloHandler.handleGameEnd(this, duelFeature, winnerUser);
                handled = true;
            } catch (NoSuchFeatureException ignored) {
            }
        }

        if (!handled) {
            if (winnerUser != null) {
                eloHandler.handleGameEnd(this, winnerUser);
            } else {
                log.warning("Could not distribute any elo!");
            }
        }
    }

    protected void loadMap() {
        // TODO this doesn't respect if a user changed the lobby in the config
        Optional<MapInfo> info = worldHandler.getMapInfo("Lobby");
        if (info.isPresent()) {
            putGameData("lobbymap", info.get());
        } else {
            abortGame();
        }
    }

    @Nonnull
    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Nonnull
    @Override
    public Phase getActivePhase() {
        return activePhase;
    }

    @Override
    public void join(@Nonnull User user) {
        if (!getActivePhase().allowJoin()) {
            spectate(user);
            return;
        }

        if (!isPlaying(user.getUuid())) {
            players.add(user);
            Bukkit.getPluginManager().callEvent(new GameJoinEvent(this, user));
            broadcastMessage(LangKey.GAME_PLAYER_JOIN, (Object) user.getDisplayName());
        }

        user.removeListeningChannel("default");
        user.addListeningChannel(chatChannel.getIdentifier());
        user.setActiveChannel(chatChannel.getIdentifier());
    }

    @Override
    public void spectate(@Nonnull User user) {
        if (!getActivePhase().allowSpectate()) {
            Lang.msg(user, LangKey.GAME_CANT_SPECTATE);
        }

        if (!isPlaying(user.getUuid()) && !isSpectating(user.getUuid())) {
            spectators.add(user);
        }
    }

    @Override
    public void leave(@Nonnull User user) {
        players.remove(user);
        spectators.remove(user);
        Bukkit.getPluginManager().callEvent(new GameLeaveEvent(this, user));
        broadcastMessage(LangKey.GAME_PLAYER_LEAVE, (Object) user.getDisplayName());

        user.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());

        user.removeListeningChannel(chatChannel.getIdentifier());
        user.addListeningChannel("default");
        user.setActiveChannel("default");
    }

    @Override
    public boolean isPlaying(@Nonnull UUID user) {
        return players.stream().anyMatch(u -> u.getUuid().equals(user));
    }

    @Override
    public boolean isSpectating(@Nonnull UUID user) {
        return spectators.stream().anyMatch(u -> u.getUuid().equals(user));
    }

    @Override
    @Nonnull
    public <T extends Feature> T createFeature(@Nonnull Class<T> featureClass, @Nonnull Phase phase) {
        T feature = injector.getInstance(featureClass);
        feature.setPhase(phase);
        feature.init();
        return feature;
    }

    @Override
    @Nonnull
    public <T extends Phase> T createPhase(@Nonnull Class<T> phaseClass) {
        T phase = injector.getInstance(phaseClass);
        phase.setGame(this);
        phase.init();
        return phase;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Nonnull
    @Override
    public List<User> getPlayers() {
        return players;
    }

    @Nonnull
    @Override
    public List<User> getSpectators() {
        return spectators;
    }

    @Nullable
    @Override
    public Object getGameData(@Nonnull String key) {
        return gameData.get(key);
    }

    @Override
    public void putGameData(@Nonnull String key, @Nonnull Object data) {
        gameData.put(key, data);
    }

    @Override
    public Duration getDuration() {
        if (duration == null) {
            return Duration.between(startTime, LocalDateTime.now());
        } else {
            return duration;
        }
    }

    @Override
    public boolean isAborting() {
        return aborted;
    }
}
