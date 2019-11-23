package com.voxelgameslib.voxelgameslib.api.stats;

import java.util.UUID;

import com.voxelgameslib.voxelgameslib.internal.lang.Locale;
import com.voxelgameslib.voxelgameslib.internal.lang.Translatable;
import com.voxelgameslib.voxelgameslib.components.user.User;

public interface Trackable {

    void setStat(Stat stat);

    Stat getStat();

    StatInstance getInstance(User user);

    StatInstance getInstance(UUID id);

    StatInstance getNewInstance(UUID uuid);

    Translatable getDisplayName();

    Translatable getText();

    StatFormatter getStatFormatter();

    String formatLong(double val, Locale locale);

    String formatShort(double val);

    String name();

    // workaround for not having access to the user handler
    User getUser(UUID id);

    Trackable[] getValues();

    boolean shouldAnnounce();

    void setAnnounce(boolean announce);

    String getPrefix();
}
