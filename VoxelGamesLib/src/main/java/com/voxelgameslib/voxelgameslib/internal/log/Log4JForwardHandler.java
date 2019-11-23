package com.voxelgameslib.voxelgameslib.internal.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class Log4JForwardHandler extends AbstractFilter {

    private LogFormatter logFormatter;

    public Log4JForwardHandler(LogFormatter logFormatter) {
        this.logFormatter = logFormatter;
    }

    @Override
    public Result filter(LogEvent logEvent) {
        logFormatter.log(logEvent.getTimeMillis(), logEvent.getLevel().name(), logEvent.getLoggerName(), logEvent.getMessage().getFormattedMessage(), logEvent.getThrown());
        return Result.DENY;
    }
}
