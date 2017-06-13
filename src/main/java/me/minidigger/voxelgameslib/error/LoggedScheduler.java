package me.minidigger.voxelgameslib.error;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

/**
 * Implements a delegating scheduler that automatically handles all exceptions.
 *
 * @author aadnk
 */
@SuppressWarnings("deprecation")
public abstract class LoggedScheduler implements BukkitScheduler {

    private class TaskedRunnable implements Runnable {

        private int taskID = -1;
        private Runnable delegate;

        public TaskedRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                delegate.run();
            } catch (Throwable e) {
                customHandler(taskID, e);
            }
        }

        public int getTaskID() {
            return taskID;
        }

        public void setTaskID(int taskID) {
            this.taskID = taskID;
        }
    }

    // A reference to the underlying scheduler
    private BukkitScheduler delegate;

    public LoggedScheduler(Plugin owner) {
        this(owner.getServer().getScheduler());
    }

    public LoggedScheduler(BukkitScheduler delegate) {
        this.delegate = delegate;
    }

    /**
     * Invoked when an error occurs in a task.
     *
     * @param taskID - unique ID of the task, or
     * @param e      - error that occured.
     */
    protected abstract void customHandler(int taskID, Throwable e);

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        return delegate.callSyncMethod(plugin, task);
    }

    @Override
    public void cancelAllTasks() {
        delegate.cancelAllTasks();
    }

    @Override
    public void cancelTask(int taskId) {
        delegate.cancelTask(taskId);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        delegate.cancelTasks(plugin);
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return delegate.getActiveWorkers();
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return delegate.getPendingTasks();
    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        return delegate.isCurrentlyRunning(taskId);
    }

    @Override
    public boolean isQueued(int taskId) {
        return delegate.isQueued(taskId);
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    public int scheduleAsyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    public int scheduleAsyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    public int scheduleAsyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay,
                                          long period) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay,
                                         long period) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable runnable) throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(runnable);

        BukkitTask bukkitTask = delegate.runTask(plugin, wrapped);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTask(Plugin plugin, BukkitRunnable bukkitRunnable)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(bukkitRunnable);

        BukkitTask bukkitTask = delegate.runTask(plugin, wrapped);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(runnable);

        BukkitTask bukkitTask = delegate.runTaskAsynchronously(plugin, wrapped);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable bukkitRunnable)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(bukkitRunnable);

        BukkitTask bukkitTask = delegate.runTaskAsynchronously(plugin, wrapped);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long l)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(runnable);

        BukkitTask bukkitTask = delegate.runTaskLater(plugin, wrapped, l);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable bukkitRunnable, long l)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(bukkitRunnable);

        BukkitTask bukkitTask = delegate.runTaskLater(plugin, wrapped, l);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long l)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(runnable);

        BukkitTask bukkitTask = delegate.runTaskLaterAsynchronously(plugin, wrapped, l);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable bukkitRunnable, long l)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(bukkitRunnable);

        BukkitTask bukkitTask = delegate.runTaskLaterAsynchronously(plugin, wrapped, l);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long l, long l1)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(runnable);

        BukkitTask bukkitTask = delegate.runTaskTimer(plugin, wrapped, l, l1);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable bukkitRunnable, long l, long l1)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(bukkitRunnable);

        BukkitTask bukkitTask = delegate.runTaskTimer(plugin, wrapped, l, l1);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long l, long l1)
            throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(runnable);

        BukkitTask bukkitTask = delegate.runTaskTimerAsynchronously(plugin, wrapped, l, l1);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable bukkitRunnable, long l,
                                                 long l1) throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(bukkitRunnable);

        BukkitTask bukkitTask = delegate.runTaskTimerAsynchronously(plugin, wrapped, l, l1);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    public void mainThreadHeartbeat(int ticks) {
        try {
            Method runSchedulerMethod = delegate.getClass().getMethod("mainThreadHeartbeat", int.class);
            runSchedulerMethod.invoke(delegate, ticks);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}