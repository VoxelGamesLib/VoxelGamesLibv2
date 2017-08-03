package com.voxelgameslib.voxelgameslib.log;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.handler.Handler;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.extern.java.Log;

@Log
@Subcommand("log")
@Singleton
public class LoggingHandler extends BaseCommand implements Handler {

    private LogHandler handler;
    private Logger logger;
    private Level level = Level.INFO;

    @Override
    public void start() {
        logger = Logger.getLogger("com.voxelgameslib.voxelgameslib");
        handler = new LogHandler() {
            @Override
            public void publish(LogRecord record) {
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

        global.addHandler(new TheFuckYouForwardHandler());
        System.setOut(new PrintStream(new TheFuckYouLoggerOutputStream(), true));
        System.setErr(new PrintStream(new TheFuckYouLoggerOutputStream(), true));
    }

    @Subcommand("log")
    @CommandAlias("log")
    @Syntax("[level] - if present, the new level")
    @CommandPermission("%admin")
    public void logcommand(User sender, @Optional String level) {
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

//    @SuppressWarnings("JavaDoc")
//    @CompleterInfo(name = "log")
//    public List<String> logcompleter(CommandArguments arguments) {
//        return CommandUtil.filterTabCompletions(arguments.getArg(0), Level.ALL.getName(), Level.CONFIG.getName(),
//                Level.FINE.getName(), Level.FINER.getName(), Level.FINEST.getName(), Level.INFO.getName()
//                , Level.OFF.getName(), Level.WARNING.getName(), Level.SEVERE.getName());
//    }TODO add completer for log command

    /**
     * Changes the log level of the logger for the framework
     *
     * @param level the new level
     */
    public void setLevel(Level level) {
        this.level = level;
        Logger.getLogger("com.voxelgameslib").setLevel(level);
        log.info("Level changed to " + level.getName());
    }


    @Override
    public void stop() {
        Logger.getLogger("").removeHandler(handler);
    }

    public Level getLevel() {
        return level;
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
