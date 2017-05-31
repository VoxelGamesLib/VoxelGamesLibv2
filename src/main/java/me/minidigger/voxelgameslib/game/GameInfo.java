package me.minidigger.voxelgameslib.game;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to identify games and collection additional data
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameInfo {

    /**
     * @return the name of the game
     */
    String name();

    /**
     * @return the author of the game
     */
    String author();

    /**
     * @return the version of the game
     */
    String version();

    /**
     * @return a short description of the game
     */
    String description();
}
