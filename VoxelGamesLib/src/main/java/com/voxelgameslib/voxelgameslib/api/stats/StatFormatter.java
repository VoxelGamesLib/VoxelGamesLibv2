package com.voxelgameslib.voxelgameslib.api.stats;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

@FunctionalInterface
public interface StatFormatter {

    StatFormatter DOUBLE = (double val) ->
            DecimalFormat.getNumberInstance().format(val);
    StatFormatter INT = (double val) ->
            DecimalFormat.getIntegerInstance().format(val);
    StatFormatter CURRENCY = (double val) ->
            DecimalFormat.getCurrencyInstance().format(val);
    StatFormatter PERCENT = (double val) ->
            DecimalFormat.getPercentInstance().format(val);
    StatFormatter DATE_TIME = (double val) ->
            DateFormat.getDateTimeInstance().format(new Date(((long) val) * 60 * 1000));
    StatFormatter DATE = (double val) ->
            DateFormat.getDateInstance().format(new Date(((long) val) * 60 * 1000));
    StatFormatter DURATION_LONG = (double val) ->
            DurationFormatUtils.formatDurationWords((long) val * 60 * 1000, true, true);
    StatFormatter DURATION_SHORT = (double val) ->
            DurationFormatUtils.formatDuration((long) val * 60 * 1000, "H:mm:ss");

    String format(double val);
}
