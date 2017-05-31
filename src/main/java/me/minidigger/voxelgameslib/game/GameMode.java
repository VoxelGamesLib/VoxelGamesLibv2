package me.minidigger.voxelgameslib.game;

import com.google.gson.annotations.Expose;
import javax.annotation.Nonnull;
import javax.persistence.Transient;
import jskills.GameRatingInfo;
import lombok.extern.java.Log;

/**
 * A {@link GameMode} is a identifier for the type of a {@link Game}.
 */
@Log
public class GameMode {

    @Expose
    private String name;

    @Transient
    private Class<? extends Game> gameClass;

    @Expose
    private String className;

    @Transient
    private GameInfo info;

    @Expose
    private GameRatingInfo ratingInfo;

    protected GameMode() {
        // JPA
    }

    /**
     * Constructs a new {@link GameMode}
     *
     * @param name      the name of this {@link GameMode}
     * @param gameClass the class that implements this {@link GameMode}
     */
    public GameMode(@Nonnull String name, @Nonnull Class<? extends Game> gameClass, GameRatingInfo ratingInfo) {
        this.name = name;
        this.gameClass = gameClass;
        className = gameClass.getName();
        this.ratingInfo = ratingInfo;
        GameInfo[] infos = gameClass.getAnnotationsByType(GameInfo.class);
        if (infos.length > 0) {
            info = infos[0];
        } else {
            log.warning("Did not found a game info annotation for class " + gameClass.getName());
        }
    }

    /**
     * @return the name of this {@link GameMode}
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * @return the class that implements this {@link GameMode}
     */
    @Nonnull
    public Class<? extends Game> getGameClass() {
        return gameClass;
    }

    /**
     * @return the game ratings info that contains all info for doing stuff with elo
     */
    public GameRatingInfo getRatingInfo() {
        return ratingInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameMode gameMode = (GameMode) o;

        return name.equals(gameMode.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Nonnull
    @Override
    public String toString() {
        return name;
    }
}
