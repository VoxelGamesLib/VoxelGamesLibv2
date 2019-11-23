package com.voxelgameslib.voxelgameslib.internal.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import javax.annotation.Nonnull;

public class JULForwardHandler extends ConsoleHandler {

    private final LogFormatter logFormatter;

    public JULForwardHandler(LogFormatter logFormatter) {
        this.logFormatter = logFormatter;
    }

    public void publish(@Nonnull LogRecord record) {
        logFormatter.log(record.getMillis(), record.getLevel().getName(), record.getLoggerName(), record.getMessage(), record.getThrown());
    }

    public void flush() {
    }

    public void close() throws SecurityException {
    }
}
