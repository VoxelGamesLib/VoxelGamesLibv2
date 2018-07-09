package com.voxelgameslib.voxelgameslib.error;

import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;

/**
 * @author aadnk
 */
@SuppressWarnings("MissingJSR305")
public abstract class LoggedPluginManager implements PluginManager {

    @SuppressWarnings("FieldCanBeLocal")
    private CommandMap commandMap;
    private Object timings;

    private PluginManager delegate;
    private Plugin owner;

    public LoggedPluginManager(Plugin owner) {
        this.owner = owner;
        this.delegate = owner.getServer().getPluginManager();
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (ReflectiveOperationException ignored) {

        }
    }

    public LoggedPluginManager(PluginManager delegate) {

    }

    /**
     * Invoked when an error occurs in a event listener.
     *
     * @param event - the event where the error occured.
     * @param e     - error that occured.
     */
    protected abstract void customHandler(Event event, Throwable e);

    /**
     * Invoked when an error occurs somewhere
     *
     * @param e the errror that occured
     */
    protected abstract void customHandler(Throwable e);

    /**
     * Registers all the events in the given listener class.
     *
     * @param listener - listener to register
     * @param plugin   - plugin to register
     */
    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException(
                    "Plugin attempted to register " + listener + " while not enabled");
        }

        // Just in case Bukkit decides to validate the parameters in the future
        EventExecutor nullExecutor = (arg0, arg1) -> {
            throw new IllegalStateException("This method should never be called!");
        };

        for (Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader()
                .createRegisteredListeners(listener, plugin).entrySet()) {

            Collection<RegisteredListener> listeners = entry.getValue();
            Collection<RegisteredListener> modified = Lists.newArrayList();

            // Use our plugin specific logger instead
            for (final RegisteredListener delegate : listeners) {
                RegisteredListener customListener = new RegisteredListener(delegate.getListener(),
                        nullExecutor, delegate.getPriority(), delegate.getPlugin(),
                        delegate.isIgnoringCancelled()) {

                    @Override
                    public void callEvent(Event event) throws EventException {
                        try {
                            delegate.callEvent(event);
                        } catch (AuthorNagException e) {
                            // Let Bukkit handle that one
                            throw e;
                        } catch (Throwable e) {
                            customHandler(event, e);
                        }
                    }
                };

                modified.add(customListener);
            }

            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(modified);
        }
    }

    private EventExecutor getWrappedExecutor(final EventExecutor executor) {
        return (listener, event) -> {
            // Just like above
            try {
                executor.execute(listener, event);
            } catch (AuthorNagException e) {
                throw e;
            } catch (Throwable e) {
                customHandler(event, e);
            }
        };
    }

    private HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;

        } catch (NoSuchMethodException e) {
            if ((clazz.getSuperclass() != null)
                    && (!clazz.getSuperclass().equals(Event.class))
                    && (Event.class.isAssignableFrom(clazz.getSuperclass()))) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(
                        Event.class));
            }
        }
        throw new IllegalPluginAccessException(
                "Unable to find handler list for event " + clazz.getName());
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority,
                              EventExecutor executor, Plugin plugin) {
        catchError(plugin, () -> delegate
                .registerEvent(event, listener, priority, getWrappedExecutor(executor), plugin));
    }

    @Override
    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority,
                              EventExecutor executor, Plugin plugin, boolean ignoreCancel) {
        catchError(plugin, () -> delegate
                .registerEvent(event, listener, priority, getWrappedExecutor(executor), plugin));
    }

    @Override
    public void registerInterface(Class<? extends PluginLoader> loader)
            throws IllegalArgumentException {
        delegate.registerInterface(loader);
    }

    @Override
    public void addPermission(Permission perm) {
        delegate.addPermission(perm);
    }

    @Override
    public void callEvent(Event event) throws IllegalStateException {
        delegate.callEvent(event);
    }

    @Override
    public void clearPlugins() {
        delegate.clearPlugins();
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        catchError(plugin, () -> delegate.disablePlugin(plugin));
    }

    @Override
    public void disablePlugin(Plugin plugin, boolean b) {
        catchError(plugin, () -> delegate.disablePlugin(plugin, b));
    }

    @Override
    public void disablePlugins() {
        delegate.disablePlugins();
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        catchError(plugin, () -> delegate.enablePlugin(plugin));
    }

    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        return delegate.getDefaultPermSubscriptions(op);
    }

    @Override
    public Set<Permission> getDefaultPermissions(boolean op) {
        return delegate.getDefaultPermissions(op);
    }

    @Override
    public Permission getPermission(String name) {
        return delegate.getPermission(name);
    }

    @Override
    public Set<Permissible> getPermissionSubscriptions(String permission) {
        return delegate.getPermissionSubscriptions(permission);
    }

    @Override
    public Set<Permission> getPermissions() {
        return delegate.getPermissions();
    }

    @Override
    public Plugin getPlugin(String name) {
        return delegate.getPlugin(name);
    }

    @Override
    public Plugin[] getPlugins() {
        return delegate.getPlugins();
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return delegate.isPluginEnabled(name);
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        return delegate.isPluginEnabled(plugin);
    }

    @Override
    public Plugin loadPlugin(File file)
            throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return delegate.loadPlugin(file);
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        return delegate.loadPlugins(directory);
    }

    @Override
    public void recalculatePermissionDefaults(Permission permission) {
        delegate.recalculatePermissionDefaults(permission);
    }

    @Override
    public void removePermission(Permission perm) {
        delegate.removePermission(perm);
    }

    @Override
    public void removePermission(String name) {
        delegate.removePermission(name);
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, Permissible permissable) {
        delegate.subscribeToDefaultPerms(op, permissable);
    }

    @Override
    public void subscribeToPermission(String permission, Permissible permissable) {
        delegate.subscribeToPermission(permission, permissable);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, Permissible permissable) {
        delegate.unsubscribeFromDefaultPerms(op, permissable);
    }

    @Override
    public void unsubscribeFromPermission(String permission, Permissible permissable) {
        delegate.unsubscribeFromPermission(permission, permissable);
    }

    @Override
    public boolean useTimings() {
        return delegate.useTimings();
    }

    private void catchError(Plugin plugin, Executor code) {
        try {
            code.execute();
        } catch (Throwable t) {
            if (plugin.getName().equalsIgnoreCase(owner.getName())) {
                customHandler(t);
            } else {
                throw t;
            }
        }
    }

    @FunctionalInterface
    interface Executor {

        void execute();
    }
}