package me.minidigger.voxelgameslib.utils.db;

import java.sql.SQLException;

import lombok.extern.java.Log;

/**
 * Template class for user to override. Will run on a different thread so you can run SQL queries
 * safely without impacting main thread. <p> Will automatically close the connection once run() is
 * done! <p> Calls onError when a SQLException is fired, and provides an onResultsSync method to be
 * overridden to receive all DB Results back on main thread, by calling getResultsSync() on the
 * Async run(DbStatement) call.
 */
@Log
public abstract class AsyncDbStatement {
    protected String query;
    private boolean done = false;

    public AsyncDbStatement() {
        queue(null);
    }

    public AsyncDbStatement(String query) {
        queue(query);
    }

    /**
     * Schedules this async statement to run on anther thread. This is the only method that should
     * be called on the main thread and it should only be called once.
     */
    private void queue(final String query) {
        this.query = query;
        AsyncDbQueue.queue(this);
    }

    /**
     * Implement this method with your code that does Async SQL logic.
     */
    protected abstract void run(DbStatement statement) throws SQLException;

    /**
     * Override this event to have special logic for when an exception is fired.
     */
    public void onError(SQLException e) {
        log.severe("Exception in AsyncDbStatement" + query);
        e.printStackTrace();
    }

    public void process(DbStatement stm) throws SQLException {
        synchronized (this) {
            if (!done) {
                if (query != null) {
                    stm.query(query);
                }
                run(stm);
                done = true;
            }
        }
    }
}
