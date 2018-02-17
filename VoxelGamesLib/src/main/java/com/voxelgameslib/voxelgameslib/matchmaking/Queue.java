package com.voxelgameslib.voxelgameslib.matchmaking;

import com.voxelgameslib.voxelgameslib.game.GameMode;

public class Queue {

    private GameMode gameMode;
    private boolean ranked;

    public Queue() {
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public boolean isRanked() {
        return this.ranked;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Queue)) return false;
        final Queue other = (Queue) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$gameMode = this.getGameMode();
        final Object other$gameMode = other.getGameMode();
        if (this$gameMode == null ? other$gameMode != null : !this$gameMode.equals(other$gameMode)) return false;
        if (this.isRanked() != other.isRanked()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $gameMode = this.getGameMode();
        result = result * PRIME + ($gameMode == null ? 43 : $gameMode.hashCode());
        result = result * PRIME + (this.isRanked() ? 79 : 97);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Queue;
    }

    public String toString() {
        return "Queue(gameMode=" + this.getGameMode() + ", ranked=" + this.isRanked() + ")";
    }
}
