package com.voxelgameslib.voxelgameslib.stats;

import java.util.UUID;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.voxelgameslib.voxelgameslib.user.User;

public interface Trackable{

    void setStat(Stat stat);

    Stat getStat();

    StatInstance getInstance(User user);

    StatInstance getInstance(UUID id);

    StatInstance getNewInstance(UUID uuid);

    String getDisplayName();

    String getText();

    StatFormatter getStatFormatter();

    String format(double val);

    String name();

    // workaround for not having access to the user handler
    User getUser(UUID id);

    Trackable[] getValues();
}
