package com.voxelgameslib.voxelgameslib.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.user.User;

import lombok.extern.java.Log;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

@Log
@Subcommand("log")
@Singleton
public class LoggingHandler extends BaseCommand implements Handler {

    private LogHandler handler;
    private Logger logger;
    private Level level = Level.INFO;
    private RollingRandomAccessFileAppender log4jAppender;

    @Override
    public void enable() {
        logger = Logger.getLogger("com.voxelgameslib.voxelgameslib");
        handler = new LogHandler() {

            @Override
            public void publish(@Nonnull LogRecord record) {
                record.setMessage("[VoxelGamesLib] " + record.getMessage());
            }
        };
        // remove old handler, fuck you reloads ;)
        Arrays.stream(logger.getHandlers()).filter(h -> h.getClass().getName().endsWith("LogHandler")).findAny().ifPresent(logger::removeHandler);

        logger.addHandler(handler);

        // fuck everyone
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }

        org.apache.logging.log4j.core.Logger log4j = (org.apache.logging.log4j.core.Logger) LogManager.getLogger("Minecraft");
        java.util.Optional<Appender> appender = log4j.getContext().getConfiguration().getAppenders().values().stream()
                .filter(app -> app instanceof RollingRandomAccessFileAppender).findAny();
        if (appender.isPresent()) {
            log4jAppender = (RollingRandomAccessFileAppender) appender.get();
        } else {
            log.warning("COULD NOT FIND LOG4j APPENDER! FILE LOGGING IS DISABLED!");
        }

        global.addHandler(new ForwardHandler(log4jAppender));
        System.setOut(new PrintStream(new LoggerOutputStream(), true));
        System.setErr(new PrintStream(new LoggerOutputStream(), true));
    }

    @Subcommand("log")
    @CommandAlias("log")
    @Syntax("[level] - if present, the new level")
    @CommandPermission("%admin")
    @CommandCompletion("ALL|CONFIG|FINE|FINER|FINEST|INFO|OFF|WARNING|SEVERE")
    public void logCommand(@Nonnull User sender, @Nullable @Optional String level) {
        if (level == null) {
            Lang.msg(sender, LangKey.LOG_LEVEL_CURRENT, logger.getLevel() == null ? "null" : logger.getLevel().getName());
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
        Logger.getLogger("").removeHandler(handler);
    }

    /**
     * Changes the log level of the logger for the framework
     *
     * @param level the new level
     */
    public void setLevel(@Nonnull Level level) {
        this.level = level;
        Logger.getLogger("com.voxelgameslib").setLevel(level);
        log.info("Level changed to " + level.getName());
    }

    // used to make the code look more clean
    private abstract class LogHandler extends java.util.logging.Handler {

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    }
}
