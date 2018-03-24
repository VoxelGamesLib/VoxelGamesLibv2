package com.voxelgameslib.voxelgameslib.stats;

import java.util.UUID;

import com.voxelgameslib.voxelgameslib.user.User;

public enum StatType implements Trackable {

    JOIN_COUNT("Join count", "joined {val} times.", false, StatFormatter.INT),
    PLAY_TIME("Play time", "played {val}.", false, StatFormatter.DURATION_LONG);

    private Stat stat;
    private String displayName;
    private String text;
    private StatFormatter statFormatter;
    private boolean announce;

    StatType(String displayName, String text, boolean announce) {
        this(displayName, text, announce, StatFormatter.DOUBLE);
    }

    StatType(String displayName, String text, boolean announce, StatFormatter statFormatter) {
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
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public StatFormatter getStatFormatter() {
        return statFormatter;
    }

    @Override
    public String formatLong(double val) {
        return text.replace("{val}", statFormatter.format(val));
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
