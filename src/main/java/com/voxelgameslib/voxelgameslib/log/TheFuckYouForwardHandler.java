package com.voxelgameslib.voxelgameslib.log;

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
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TheFuckYouForwardHandler extends ConsoleHandler {

    //    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("kk:mm:ss:SSS");
    //maybe change vanilla log format to include millis too <-- hell no
    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("kk:mm:ss");
    private final PrintStream sout = new PrintStream(new FileOutputStream(FileDescriptor.out));
    private final RollingRandomAccessFileAppender log4jAppender;

    public TheFuckYouForwardHandler(@Nullable RollingRandomAccessFileAppender log4jAppender) {
        this.log4jAppender = log4jAppender;
    }

    public void publish(@Nonnull LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(hourFormat.format(new Date(record.getMillis()))).append(" ").append(record.getLevel().getName()).append("]: ");
        sb.append(record.getMessage());
        sout.println(sb.toString());
        if (log4jAppender != null) {
            Message log4jMessage = new MutableLogEvent(new StringBuilder(record.getMessage()), new Object[0]);
            LogEvent log4jEvent = Log4jLogEvent.newBuilder().setMessage(log4jMessage).setTimeMillis(record.getMillis()).setLevel(toLog4j(record.getLevel())).build();
            log4jAppender.append(log4jEvent);
        }

        if (record.getThrown() != null) {
            record.getThrown().printStackTrace(sout);
            if (log4jAppender != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                Message log4jMessage = new MutableLogEvent(new StringBuilder(sw.toString()), new Object[0]);
                LogEvent log4jEvent = Log4jLogEvent.newBuilder().setMessage(log4jMessage).setTimeMillis(record.getMillis()).setLevel(toLog4j(record.getLevel())).build();
                log4jAppender.append(log4jEvent);
            }
        }

        super.publish(record);
    }

    private org.apache.logging.log4j.Level toLog4j(@Nonnull Level level) {
        switch (level.getName()) {
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

    public void flush() {
    }

    public void close() throws SecurityException {
    }
}