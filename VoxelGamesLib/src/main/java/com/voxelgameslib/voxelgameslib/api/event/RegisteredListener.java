package com.voxelgameslib.voxelgameslib.api.event;

import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.game.Game;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class RegisteredListener {

    private Listener listener;
    private Game game;
    private Class<Event> eventClass;
    private Method method;
    private List<EventFilter> filters;

    @java.beans.ConstructorProperties({"listener", "game", "eventClass", "method", "filters"})
    public RegisteredListener(Listener listener, Game game, Class<Event> eventClass, Method method, List<EventFilter> filters) {
        this.listener = listener;
        this.game = game;
        this.eventClass = eventClass;
        this.method = method;
        this.filters = filters;
    }

    public void addFilter(@Nonnull EventFilter filter) {
        filters.add(filter);
    }

    public Listener getListener() {
        return this.listener;
    }

    public Game getGame() {
        return this.game;
    }

    public Class<Event> getEventClass() {
        return this.eventClass;
    }

    public Method getMethod() {
        return this.method;
    }

    public List<EventFilter> getFilters() {
        return this.filters;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setEventClass(Class<Event> eventClass) {
        this.eventClass = eventClass;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setFilters(List<EventFilter> filters) {
        this.filters = filters;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RegisteredListener)) return false;
        final RegisteredListener other = (RegisteredListener) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$listener = this.getListener();
        final Object other$listener = other.getListener();
        if (this$listener == null ? other$listener != null : !this$listener.equals(other$listener)) return false;
        final Object this$game = this.getGame();
        final Object other$game = other.getGame();
        if (this$game == null ? other$game != null : !this$game.equals(other$game)) return false;
        final Object this$eventClass = this.getEventClass();
        final Object other$eventClass = other.getEventClass();
        if (this$eventClass == null ? other$eventClass != null : !this$eventClass.equals(other$eventClass))
            return false;
        final Object this$method = this.getMethod();
        final Object other$method = other.getMethod();
        if (this$method == null ? other$method != null : !this$method.equals(other$method)) return false;
        final Object this$filters = this.getFilters();
        final Object other$filters = other.getFilters();
        if (this$filters == null ? other$filters != null : !this$filters.equals(other$filters)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $listener = this.getListener();
        result = result * PRIME + ($listener == null ? 43 : $listener.hashCode());
        final Object $game = this.getGame();
        result = result * PRIME + ($game == null ? 43 : $game.hashCode());
        final Object $eventClass = this.getEventClass();
        result = result * PRIME + ($eventClass == null ? 43 : $eventClass.hashCode());
        final Object $method = this.getMethod();
        result = result * PRIME + ($method == null ? 43 : $method.hashCode());
        final Object $filters = this.getFilters();
        result = result * PRIME + ($filters == null ? 43 : $filters.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RegisteredListener;
    }

    public String toString() {
        return "RegisteredListener(listener=" + this.getListener() + ", game=" + this.getGame() + ", eventClass=" + this.getEventClass() + ", method=" + this.getMethod() + ", filters=" + this.getFilters() + ")";
    }
}
