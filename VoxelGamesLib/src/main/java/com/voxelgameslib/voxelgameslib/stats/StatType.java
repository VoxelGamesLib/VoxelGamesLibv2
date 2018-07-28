package com.voxelgameslib.voxelgameslib.stats;

import java.util.UUID;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.lang.Locale;
import com.voxelgameslib.voxelgameslib.lang.Translatable;
import com.voxelgameslib.voxelgameslib.user.User;

public enum StatType implements Trackable {

    JOIN_COUNT(LangKey.STAT_JOIN_COUNT_NAME, LangKey.STAT_JOIN_COUNT_TEXT, false, StatFormatter.INT),
    PLAY_TIME(LangKey.STAT_PLAY_TIME_NAME, LangKey.STAT_PLAY_TIME_TEXT, false, StatFormatter.DURATION_LONG);

    private Stat stat;
    private Translatable displayName;
    private Translatable text;
    private StatFormatter statFormatter;
    private boolean announce;

    StatType(Translatable displayName, Translatable text, boolean announce) {
        this(displayName, text, announce, StatFormatter.DOUBLE);
    }

    StatType(Translatable displayName, Translatable text, boolean announce, StatFormatter statFormatter) {
        this.displayName = displayName;
        this.text = text;
        this.announce = announce;
        this.statFormatter = statFormatter;
    }

    @Override
    public void setStat(Stat stat) {
        this.stat = stat;
    }

    @Override
    public Stat getStat() {
        return stat;
    }

    @Override
    public StatInstance getInstance(User user) {
        return stat.getInstance(user);
    }

    @Override
    public StatInstance getInstance(UUID id) {
        return stat.getInstance(id);
    }

    @Override
    public StatInstance getNewInstance(UUID uuid) {
        return stat.getNewInstance(uuid);
    }

    @Override
    public Translatable getDisplayName() {
        return displayName;
    }

    @Override
    public Translatable getText() {
        return text;
    }

    @Override
    public StatFormatter getStatFormatter() {
        return statFormatter;
    }

    @Override
    public String formatLong(double val, Locale locale) {
        return Lang.string(text, locale, statFormatter.format(val));
    }

    @Override
    public String formatShort(double val) {
        return statFormatter.format(val);
    }

    // workaround for not having access to the user handler
    @Override
    public User getUser(UUID id) {
        return stat.getUser(id);
    }

    @Override
    public Trackable[] getValues() {
        return values();
    }

    @Override
    public boolean shouldAnnounce() {
        return announce;
    }

    @Override
    public void setAnnounce(boolean announce) {
        this.announce = announce;
    }

    @Override
    public String getPrefix() {
        return "VGL";
    }
}
