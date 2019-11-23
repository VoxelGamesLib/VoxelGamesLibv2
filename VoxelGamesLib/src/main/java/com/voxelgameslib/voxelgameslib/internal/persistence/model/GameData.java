package com.voxelgameslib.voxelgameslib.internal.persistence.model;

import com.google.gson.annotations.Expose;

import org.hibernate.annotations.Type;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "games")
public class GameData {

    @Expose
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Expose
    @ElementCollection
    @Type(type = "uuid-char")
    @CollectionTable(name = "games_players")
    private List<UUID> players;

    @Expose
    @ElementCollection
    @Type(type = "uuid-char")
    @CollectionTable(name = "games_spectators")
    private List<UUID> spectators;

    @Expose
    private Duration duration;

    @Expose
    @Nullable
    private UUID winner;

    @Expose
    @Nullable
    @ElementCollection
    @Type(type = "uuid-char")
    @CollectionTable(name = "games_winners")
    private List<UUID> winners;

    @Expose
    private String gameMode;

    @Expose
    private boolean aborted;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public void setSpectators(List<UUID> spectators) {
        this.spectators = spectators;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Nullable
    public UUID getWinner() {
        return winner;
    }

    public void setWinner(@Nullable UUID winner) {
        this.winner = winner;
    }

    @Nullable
    public List<UUID> getWinners() {
        return winners;
    }

    public void setWinners(@Nullable List<UUID> winners) {
        this.winners = winners;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        return aborted == gameData.aborted &&
                Objects.equals(id, gameData.id) &&
                Objects.equals(players, gameData.players) &&
                Objects.equals(spectators, gameData.spectators) &&
                Objects.equals(duration, gameData.duration) &&
                Objects.equals(winner, gameData.winner) &&
                Objects.equals(winners, gameData.winners) &&
                Objects.equals(gameMode, gameData.gameMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, players, spectators, duration, winner, winners, gameMode, aborted);
    }

    @Override
    public String toString() {
        return "GameData{" +
                "id=" + id +
                ", players=" + players +
                ", spectators=" + spectators +
                ", duration=" + duration +
                ", winner=" + winner +
                ", winners=" + winners +
                ", gameMode=" + gameMode +
                ", aborted=" + aborted +
                '}';
    }
}
