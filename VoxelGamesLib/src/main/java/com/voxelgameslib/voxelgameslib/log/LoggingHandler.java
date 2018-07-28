package com.voxelgameslib.voxelgameslib.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.error.ErrorHandler;
import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

@Subcommand("log")
@Singleton
public class LoggingHandler extends BaseCommand implements Handler {

    private static final Logger log = Logger.getLogger(LoggingHandler.class.getName());
    private Logger parent;
    private Level level = Level.INFO;

    private ErrorHandler errorHandler;

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void enable() {
        System.out.println("[VoxelGamesLib] Taking over logging...");
        if (errorHandler == null) {
            System.err.println("ERRORHANDLER IS NULL, ABORTING");
            return;
        }

        // force everybody to use the parent handler
        java.util.logging.LogManager manager = java.util.logging.LogManager.getLogManager();
        Enumeration<String> names = manager.getLoggerNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (name.startsWith("com.voxelgameslib")) {
                Logger logger = Logger.getLogger(name);
                logger.setUseParentHandlers(true);
            }
        }
        parent = Logger.getLogger("com.voxelgameslib");

        // get file logger
        org.apache.logging.log4j.core.Logger log4j = (org.apache.logging.log4j.core.Logger) LogManager.getLogger("Minecraft");
        java.util.Optional<Appender> appender = log4j.getContext().getConfiguration().getAppenders().values().stream()
                .filter(app -> app instanceof RollingRandomAccessFileAppender).findAny();
        RollingRandomAccessFileAppender log4jAppender = null;
        if (appender.isPresent()) {
            log4jAppender = (RollingRandomAccessFileAppender) appender.get();
        } else {
            log.warning("COULD NOT FIND LOG4j APPENDER! FILE LOGGING IS DISABLED!");
        }
        LogFormatter logFormatter = new LogFormatter(log4jAppender, errorHandler);

        // fuck everyone

        // jul first
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        // log4j later
        log4j.getContext().getConfiguration().getAppenders().values().forEach(log4j::removeAppender);

        // forward

        // get all log4j messages and let them go thru our handler
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new Log4JForwardHandler(logFormatter));

        // get all jul messages and let them go thru our handler
        global.addHandler(new JULForwardHandler(logFormatter));

        // get all sout messages and let them go thru out handler (via jul)
        System.setOut(new PrintStream(new SoutForwardHandler(), true));
        System.setErr(new PrintStream(new SoutForwardHandler(), true));
    }

    @Subcommand("log")
    @CommandAlias("log")
    @Syntax("[level] - if present, the new level")
    @CommandPermission("%admin")
    @CommandCompletion("ALL|CONFIG|FINE|FINER|FINEST|INFO|OFF|WARNING|SEVERE")
    public void logCommand(@Nonnull User sender, @Nullable @Optional String level) {
        if (level == null) {
            Lang.msg(sender, LangKey.LOG_LEVEL_CURRENT, getLevel().getName());
            return;
        }

        try {
            setLevel(Level.parse(level));
            Lang.msg(sender, LangKey.LOG_LEVEL_SET, level);
        } catch (IllegalArgumentException ex) {
            Lang.msg(sender, LangKey.LOG_LEVEL_UNKNOWN, level);
        }
    }

    @Nonnull
    public Level getLevel() {
        return level;
    }


    @Override
    public void disable() {

    }

    /**
     * Changes the log level of the logger for the framework
     *
     * @param level the new level
     */
    public void setLevel(@Nonnull Level level) {
        this.level = level;
        parent.setLevel(level);
        log.info("Level changed to " + level.getName());
    }
}
