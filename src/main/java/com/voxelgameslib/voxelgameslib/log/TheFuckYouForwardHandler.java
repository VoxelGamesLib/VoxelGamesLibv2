package com.voxelgameslib.voxelgameslib.log;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class TheFuckYouForwardHandler extends ConsoleHandler {

    //    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("kk:mm:ss:SSS");
    //maybe change vanilla log format to include millis too <-- hell no
    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("kk:mm:ss");
    private final PrintStream sout = new PrintStream(new FileOutputStream(FileDescriptor.out));

    public void publish(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(hourFormat.format(new Date(record.getMillis()))).append(" ").append(record.getLevel().getName()).append("]: ");
        sb.append(record.getMessage());
        sout.println(sb.toString());

        if (record.getThrown() != null) {
            record.getThrown().printStackTrace(sout);
        }
    }

    public void flush() {
    }

    public void close() throws SecurityException {
    }
}