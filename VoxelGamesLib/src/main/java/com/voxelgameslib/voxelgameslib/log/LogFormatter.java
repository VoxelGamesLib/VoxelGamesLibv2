package com.voxelgameslib.voxelgameslib.log;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.message.Message;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LogFormatter {

    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("kk:mm:ss:SSS");
    private final PrintStream sout = new PrintStream(new FileOutputStream(FileDescriptor.out));
    @Nullable
    private final RollingRandomAccessFileAppender log4jAppender;

    public LogFormatter(@Nullable RollingRandomAccessFileAppender log4jAppender){
        this.log4jAppender = log4jAppender;
    }

    public void log(long millies, String levelName, String loggerName, String message, @Nullable Throwable throwable){
        levelName = formatLevel(levelName, message);
        loggerName = formatLoggerName(loggerName, message);
        message = formatMessage(message);

        // print out to sout
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(hourFormat.format(new Date(millies))).append(" ").append(levelName).append("]: ");
        sb.append("<").append(loggerName).append("> ");
        sb.append(message);
        sout.println(sb.toString());

        // print to log4j to handle file stuff
        if (log4jAppender != null) {
            Message log4jMessage = new MutableLogEvent(new StringBuilder(message), new Object[0]);
            LogEvent log4jEvent = Log4jLogEvent.newBuilder().setMessage(log4jMessage).setTimeMillis(millies).setLevel(toLog4j(levelName)).setLoggerName(loggerName).build();
            log4jAppender.append(log4jEvent);
        }

        if (throwable != null) {
            // print to sout
            throwable.printStackTrace(sout);

            // print to log4j to handle file stuff
            if (log4jAppender != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                Message log4jMessage = new MutableLogEvent(new StringBuilder(sw.toString()), new Object[0]);
                LogEvent log4jEvent = Log4jLogEvent.newBuilder().setMessage(log4jMessage).setTimeMillis(millies).setLevel(toLog4j(levelName)).setLoggerName(loggerName).build();
                log4jAppender.append(log4jEvent);
            }
        }
    }

    @Nonnull
    private org.apache.logging.log4j.Level toLog4j(String level) {
        switch (level) {
            case "SEVERE":
                return org.apache.logging.log4j.Level.ERROR;
            case "WARNING":
                return org.apache.logging.log4j.Level.WARN;
            case "INFO":
                return org.apache.logging.log4j.Level.INFO;
            case "CONFIG":
                return org.apache.logging.log4j.Level.DEBUG;
            case "FINE":
                return org.apache.logging.log4j.Level.DEBUG;
            case "FINER":
                return org.apache.logging.log4j.Level.TRACE;
            case "FINEST":
                return org.apache.logging.log4j.Level.ALL;
            default:
                return org.apache.logging.log4j.Level.INFO;
        }
    }

    private String formatLevel(String level, String msg) {
        if(msg.startsWith("Hibernate:")){
            return "FINER ";
        }
        return StringUtils.rightPad(level.replace("WARNING", "WARN"), 6);
    }

    private String formatMessage(String message){
        if(message.startsWith("Hibernate:")){
            return message.replace("Hibernate: ","");
        }
        return message;
    }

    private String formatLoggerName(@Nullable String name, String msg) {
        if (name == null || name.length() == 0) {
            if(msg.startsWith("Hibernate:")){
                return "Hibernate    ";
            }

            return "Unknown      ";
        }

        if (name.contains("voxelgameslib")) {
            return "VoxelGamesLib";
        } else if (name.contains("hibernate")) {
            return "Hibernate    ";
        }else if(name.contains("net.minecraft.server")){
            return "Minecraft    ";
        }

        return StringUtils.rightPad(name, 13);
    }
}
