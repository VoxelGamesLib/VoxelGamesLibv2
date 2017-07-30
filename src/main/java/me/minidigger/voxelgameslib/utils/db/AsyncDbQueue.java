package me.minidigger.voxelgameslib.utils.db;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.java.Log;

@Log
public class AsyncDbQueue implements Runnable {
    private static final Queue<AsyncDbStatement> queue = new ConcurrentLinkedQueue<>();
    private static final Lock lock = new ReentrantLock();

    @Override
    public void run() {
        processQueue();
    }

    public static void processQueue() {
        if (queue.isEmpty() || !lock.tryLock()) {
            return;
        }

        AsyncDbStatement stm = null;
        DbStatement dbStatement;

        try {
            dbStatement = new DbStatement();
        } catch (Exception e) {
            lock.unlock();
            log.severe("Exception getting DBStatement in AsyncDbQueue");
            e.printStackTrace();
            return;
        }

        while ((stm = queue.poll()) != null) {
            try {
                if (dbStatement.isClosed()) {
                    dbStatement = new DbStatement();
                }
                stm.process(dbStatement);
            } catch (SQLException e) {
                stm.onError(e);
            }
        }
        dbStatement.close();
        lock.unlock();
    }

    public static boolean queue(AsyncDbStatement stm) {
        return queue.offer(stm);
    }

}
