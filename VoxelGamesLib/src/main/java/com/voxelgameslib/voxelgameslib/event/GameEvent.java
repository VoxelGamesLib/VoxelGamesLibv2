package com.voxelgameslib.voxelgameslib.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameEvent {

    /**
     * @return True when this method should only be called when the player is playing in the right game
     */
    boolean filterPlayers() default true;

    /**
     * @return True when this method should only be called when the player is spectating in the right game
     */
    boolean filterSpectators() default false;
}
