package com.voxelgameslib.voxelgameslib.utils.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import co.aikar.timings.lib.TimingManager;
import lombok.extern.java.Log;

@SuppressWarnings("MissingJSR305")
@Log
public final class DB {
    private static HikariDataSource pooledDataSource;
    static TimingManager timingManager;
    static Plugin plugin;

    private DB() {
    }

    /**
     * Called in onDisable, destroys the Data source and nulls out references.
     */
    public static void close() {
        AsyncDbQueue.processQueue();
        pooledDataSource.close();
        pooledDataSource = null;
    }

    /**
     * Called in onEnable, initializes the pool and configures it and opens the first connection to
     * spawn the pool.
     */
    public static void initialize(Plugin plugin) {
        try {
            DB.plugin = plugin;
            timingManager = TimingManager.of(plugin);
            HikariConfig config = new HikariConfig();
            config.setPoolName("VGL-Client");

            String jdbc = plugin.getConfig().getString("db.jdbc");
            log.info("Connecting to Database: " + jdbc);
            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("url", "jdbc:mysql://" + jdbc + "/" + plugin.getConfig().getString("db.name"));
            config.addDataSourceProperty("user", plugin.getConfig().getString("db.username"));
            config.addDataSourceProperty("password", plugin.getConfig().getString("db.password"));
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("useServerPrepStmts", true);
            config.addDataSourceProperty("cacheCallableStmts", true);
            config.addDataSourceProperty("cacheResultSetMetadata", true);
            config.addDataSourceProperty("cacheServerConfiguration", true);
            config.addDataSourceProperty("useLocalSessionState", true);
            config.addDataSourceProperty("elideSetAutoCommits", true);
            config.addDataSourceProperty("alwaysSendSetIsolation", false);

            config.setConnectionTestQuery("SELECT 1");
            config.setInitializationFailFast(true);
            config.setMinimumIdle(3);
            config.setMaximumPoolSize(5);

            pooledDataSource = new HikariDataSource(config);
            pooledDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new AsyncDbQueue(), 0, 1);
        } catch (Exception ex) {
            pooledDataSource = null;
            log.severe("Error creating database pool");
            ex.printStackTrace();
            //Bukkit.getServer().shutdown(); // let's not...
        }
    }

    /**
     * Initiates a new DbStatement and prepares the first query. <p> YOU MUST MANUALLY CLOSE THIS
     * STATEMENT IN A finally {} BLOCK!
     */
    public static DbStatement query(@Language("MySQL") String query) throws SQLException {
        return (new DbStatement()).query(query);
    }

    /**
     * Utility method to execute a query and retrieve the first row, then close statement. You
     * should ensure result will only return 1 row for maximum performance.
     *
     * @param query  The query to run
     * @param params The parameters to execute the statement with
     * @return DbRow of your results (HashMap with template return type)
     */
    public static DbRow getFirstRow(@Language("MySQL") String query, Object... params) throws SQLException {
        try (DbStatement statement = DB.query(query).execute(params)) {
            return statement.getNextRow();
        }
    }

    /**
     * Utility method to execute a query and retrieve the first column of the first row, then close
     * statement. You should ensure result will only return 1 row for maximum performance.
     *
     * @param query  The query to run
     * @param params The parameters to execute the statement with
     * @return DbRow of your results (HashMap with template return type)
     */
    public static <T> T getFirstColumn(@Language("MySQL") String query, Object... params) throws SQLException {
        try (DbStatement statement = DB.query(query).execute(params)) {
            return statement.getFirstColumn();
        }
    }

    /**
     * Utility method to execute a query and retrieve first column of all results, then close
     * statement. <p> Meant for single queries that will not use the statement multiple times.
     */
    public static <T> List<T> getFirstColumnResults(@Language("MySQL") String query, Object... params) throws SQLException {
        List<T> dbRows = new ArrayList<>();
        T result;
        try (DbStatement statement = DB.query(query).execute(params)) {
            while ((result = statement.getFirstColumn()) != null) {
                dbRows.add(result);
            }
        }
        return dbRows;
    }

    /**
     * Utility method to execute a query and retrieve all results, then close statement. <p> Meant
     * for single queries that will not use the statement multiple times.
     *
     * @param query  The query to run
     * @param params The parameters to execute the statement with
     * @return List of DbRow of your results (HashMap with template return type)
     */
    public static List<DbRow> getResults(@Language("MySQL") String query, Object... params) throws SQLException {
        try (DbStatement statement = DB.query(query).execute(params)) {
            return statement.getResults();
        }
    }

    /**
     * Utility method for executing an update synchronously that does an insert, closes the
     * statement, and returns the last insert ID.
     *
     * @param query  Query to run
     * @param params Params to execute the statement with.
     * @return Inserted Row Id.
     */
    public static Long executeInsert(@Language("MySQL") String query, Object... params) throws SQLException {
        try (DbStatement statement = DB.query(query)) {
            int i = statement.executeUpdate(params);
            if (i > 0) {
                return statement.getLastInsertId();
            }
        }
        return null;
    }

    /**
     * Utility method for executing an update synchronously, and then close the statement.
     *
     * @param query  Query to run
     * @param params Params to execute the statement with.
     * @return Number of rows modified.
     */
    public static int executeUpdate(@Language("MySQL") String query, Object... params) throws SQLException {
        try (DbStatement statement = DB.query(query)) {
            return statement.executeUpdate(params);
        }
    }

    /**
     * Utility method to execute an update statement asynchronously and close the connection.
     *
     * @param query  Query to run
     * @param params Params to execute the update with
     */
    public static void executeUpdateAsync(@Language("MySQL") String query, final Object... params) {
        new AsyncDbStatement(query) {
            @Override
            public void run(DbStatement statement) throws SQLException {
                statement.executeUpdate(params);
            }
        };
    }

    static Connection getConnection() throws SQLException {
        return pooledDataSource != null ? pooledDataSource.getConnection() : null;
    }

    public static void createTransactionAsync(TransactionCallback run) {
        createTransactionAsync(run, null, null);
    }

    public static void createTransactionAsync(TransactionCallback run, Runnable onSuccess, Runnable onFail) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!createTransaction(run)) {
                if (onFail != null) {
                    onFail.run();
                }
            } else if (onSuccess != null) {
                onSuccess.run();
            }
        });
    }

    public static boolean createTransaction(TransactionCallback run) {
        try (DbStatement stm = new DbStatement()) {
            try {
                stm.startTransaction();
                if (!run.apply(stm)) {
                    stm.rollback();
                    return false;
                } else {
                    stm.commit();
                    return true;
                }
            } catch (Exception e) {
                stm.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface TransactionCallback extends Function<DbStatement, Boolean> {
        @Override
        default Boolean apply(DbStatement dbStatement) {
            try {
                return this.runTransaction(dbStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        Boolean runTransaction(DbStatement stm) throws SQLException;
    }
}
