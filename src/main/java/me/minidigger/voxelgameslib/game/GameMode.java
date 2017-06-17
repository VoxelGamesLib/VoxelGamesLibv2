package me.minidigger.voxelgameslib.game;

import com.google.gson.annotations.Expose;

import javax.annotation.Nonnull;

import lombok.extern.java.Log;

/**
 * A {@link GameMode} is a identifier for the type of a {@link Game}.
 */
@Log
public class GameMode extends jskills.GameInfo {

    private static final jskills.GameInfo defaultGameInfo = jskills.GameInfo.getDefaultGameInfo();

    @Expose
    private String name;

    private Class<? extends Game> gameClass;
    @Expose
    private String className;

    private GameInfo info;

    /**
     * Constructs a new {@link GameMode}
     *
     * @param name      the name of this {@link GameMode}
     * @param gameClass the class that implements this {@link GameMode}
     */
    public GameMode(@Nonnull String name, @Nonnull Class<? extends Game> gameClass,
                    double initialMean, double initialStandardDeviation,
                    double beta, double dynamicFactor, double drawProbability) {
        super(initialMean, initialStandardDeviation, beta, dynamicFactor, drawProbability);
        this.name = name;
        this.gameClass = gameClass;
        this.className = gameClass.getName();
        GameInfo[] infos = gameClass.getAnnotationsByType(GameInfo.class);
        if (infos.length > 0) {
            info = infos[0];
        } else {
            log.warning("Did not found a game info annotation for class " + gameClass.getName());
        }
    }

    /**
     * Constructs a new {@link GameMode}
     *
     * @param name      the name of this {@link GameMode}
     * @param gameClass the class that implements this {@link GameMode}
     */
    public GameMode(@Nonnull String name, @Nonnull Class<? extends Game> gameClass) {
        this(name, gameClass, defaultGameInfo.getInitialMean(),
                defaultGameInfo.getInitialStandardDeviation(), defaultGameInfo.getBeta(),
                defaultGameInfo.getDynamicsFactor(), defaultGameInfo.getDrawProbability());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

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
