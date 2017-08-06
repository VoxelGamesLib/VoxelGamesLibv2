package com.voxelgameslib.voxelgameslib.game;

import com.google.inject.Injector;

import com.voxelgameslib.voxelgameslib.chat.ChatChannel;
import com.voxelgameslib.voxelgameslib.chat.ChatHandler;
import com.voxelgameslib.voxelgameslib.components.team.Team;
import com.voxelgameslib.voxelgameslib.elo.EloHandler;
import com.voxelgameslib.voxelgameslib.event.events.game.GameEndEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameLeaveEvent;
import com.voxelgameslib.voxelgameslib.exception.NoSuchFeatureException;
import com.voxelgameslib.voxelgameslib.feature.Feature;
import com.voxelgameslib.voxelgameslib.feature.features.DuelFeature;
import com.voxelgameslib.voxelgameslib.feature.features.TeamFeature;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.lang.Translatable;
import com.voxelgameslib.voxelgameslib.map.MapInfo;
import com.voxelgameslib.voxelgameslib.phase.Phase;
import com.voxelgameslib.voxelgameslib.tick.TickHandler;
import com.voxelgameslib.voxelgameslib.user.PlayerState;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.world.WorldHandler;

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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.bukkit.Bukkit;

import lombok.extern.java.Log;

/**
 * Abstract implementation of a {@link Game}. Handles broadcasting, ticking and user management.
 */
@Log
@Entity(name = "Game")
@Table(name = "games")
public abstract class AbstractGame implements Game {

    @Inject
    @Transient
    private Injector injector;
    @Inject
    @Transient
    private TickHandler tickHandler;
    @Inject
    @Transient
    private GameHandler gameHandler;
    @Inject
    @Transient
    private EloHandler eloHandler;
    @Inject
    @Transient
    private WorldHandler worldHandler;
    @Inject
    @Transient
    private ChatHandler chatHandler;

    @Nonnull
    @Transient // todo: save this in the entity
    private GameMode gameMode;

    @Transient
    protected Phase activePhase;

    @Id
    private UUID uuid;

    @Column(name = "min_players")
    private int minPlayers;
    @Column(name = "max_players")
    private int maxPlayers;

    @Transient // todo: save this in the entity, relation to User as well
    private final List<User> players = new ArrayList<>();
    @Transient // todo: save this in the entity, relation to User as well
    private final List<User> spectators = new ArrayList<>();
    @Transient
    private final List<User> allUsers = new ArrayList<>();

    @Transient
    // todo: save this in the entity, see: @Convert annotation (https://stackoverflow.com/questions/25738569/jpa-map-json-column-to-java-object)
    private Map<Class<GameData>, GameData> gameData = new HashMap<>();

    private boolean aborted = false;

    @Column(name = "start_time")
    private LocalDateTime startTime;
    private Duration duration;

    @Transient
    private ChatChannel chatChannel;

    @Transient
    private Map<UUID, PlayerState> playerStates = new HashMap<>();

    /**
     * Constructs a new {@link AbstractGame}
     *
     * @param mode the mode this {@link Game} is an instance of.
     */
    public AbstractGame(@Nonnull GameMode mode) {
        this.gameMode = mode;
    }

    protected AbstractGame() {
        //JPA
    }

    @Override
    public void setUuid(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void broadcastMessage(@Nonnull Component message) {
        allUsers.forEach(u -> u.sendMessage(message));
    }

    @Override
    public void broadcastMessage(@Nonnull Translatable key, @Nullable Object... args) {
        allUsers.forEach(user -> Lang.msg(user, key, args));
    }

    @Override
    public void start() {
        startTime = LocalDateTime.now();
        chatChannel = chatHandler.createChannel("game." + getUuid().toString());

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
        if (activePhase.isRunning()) {
            activePhase.tick();
        }
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
            List<User> winningUsers = new ArrayList<>();
            winningUsers.add(winnerUser);

            Bukkit.getPluginManager().callEvent(new GameEndEvent(this, winningUsers, duration, aborted));
        } else {
            Bukkit.getPluginManager()
                    .callEvent(new GameEndEvent(this, new ArrayList<>(), duration, aborted));
        }

        if (!aborted) {
            broadcastMessage(LangKey.GAME_END);
        }

        end();
    }

    private void end() {
        while (players.size() != 0) {
            leave(players.get(0));
        }
        while (spectators.size() != 0) {
            leave(spectators.get(0));
        }

        if (activePhase.isRunning()) {
            activePhase.setRunning(false);
            activePhase.stop();
        }

        chatHandler.removeChannel(chatChannel.getIdentifier());
        chatChannel = null;

        tickHandler.end(this);
        gameHandler.removeGame(this);
    }

    @Override
    public void abortGame() {
        log.finer("abort  game");

        aborted = true;
        broadcastMessage(LangKey.GAME_ABORT);

        endGame(null, null);
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
        loadMap("Lobby");
    }

    protected void loadMap(@Nonnull String name) {
        // TODO this doesn't respect if a user changed the lobby in the config
        Optional<MapInfo> info = worldHandler.getMapInfo(name);
        if (info.isPresent()) {
            DefaultGameData gameData = getGameData(DefaultGameData.class).orElse(new DefaultGameData());
            gameData.lobbyMap = info.get();
            putGameData(gameData);
        } else {
            log.warning("Could not find map " + name);
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
    public boolean join(@Nonnull User user) {
        if (!getActivePhase().allowJoin() || getMaxPlayers() == players.size()) {
            return spectate(user);
        }

        if (!isPlaying(user.getUuid())) {
            players.add(user);
            allUsers.add(user);
            playerStates.put(user.getUuid(), PlayerState.of(user));
            GameJoinEvent event = new GameJoinEvent(this, user);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                players.remove(user);
                allUsers.remove(user);
                return false;
            }
            broadcastMessage(LangKey.GAME_PLAYER_JOIN, (Object) user.getDisplayName());
        }

        // todo: perhaps consider having this handled by phases instead
        user.removeListeningChannel(chatHandler.defaultChannel.getIdentifier()); // stop listening to global messages
        user.addListeningChannel(chatChannel.getIdentifier()); // local channel
        user.setActiveChannel(chatChannel.getIdentifier());
        return true;
    }

    @Override
    public boolean spectate(@Nonnull User user) {
        if (!getActivePhase().allowSpectate()) {
            Lang.msg(user, LangKey.GAME_CANT_SPECTATE);
            return false;
        }

        if (!isPlaying(user.getUuid()) && !isSpectating(user.getUuid())) {
            spectators.add(user);
            allUsers.add(user);
            playerStates.put(user.getUuid(), PlayerState.of(user));
            return true;
        }
        return false;
    }

    @Override
    public void leave(@Nonnull User user) {
        players.remove(user);
        spectators.remove(user);
        allUsers.remove(user);
        Optional.ofNullable(playerStates.remove(user.getUuid())).ifPresent(state -> state.apply(user));
        Bukkit.getPluginManager().callEvent(new GameLeaveEvent(this, user));
        broadcastMessage(LangKey.GAME_PLAYER_LEAVE, (Object) user.getDisplayName());

        user.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());

        user.removeListeningChannel(chatChannel.getIdentifier());
        user.addListeningChannel(chatHandler.defaultChannel.getIdentifier());
        user.setActiveChannel(chatHandler.defaultChannel.getIdentifier());
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
    public boolean isParticipating(@Nonnull UUID user) {
        return allUsers.stream().anyMatch(u -> u.getUuid().equals(user));
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

    @Nonnull
    @Override
    public List<User> getAllUsers() {
        return allUsers;
    }

    @Nonnull
    @Override
    public <T extends GameData> Optional<T> getGameData(@Nonnull Class<T> key) {
        //noinspection unchecked,SuspiciousMethodCalls
        return Optional.ofNullable((T) gameData.get(key));
    }

    @Override
    public void putGameData(@Nonnull GameData data) {
        //noinspection unchecked
        gameData.put((Class<GameData>) data.getClass(), data);
    }

    @Override
    @Nonnull
    public Duration getDuration() {
        if (duration == null) {
            return duration = Duration.between(startTime, LocalDateTime.now());
        } else {
            return duration;
        }
    }

    @Override
    public boolean isAborting() {
        return aborted;
    }
}
