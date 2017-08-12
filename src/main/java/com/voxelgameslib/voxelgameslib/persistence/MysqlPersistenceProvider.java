package com.voxelgameslib.voxelgameslib.persistence;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.user.GamePlayer;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.db.DB;
import com.voxelgameslib.voxelgameslib.utils.db.DbRow;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.java.Log;

@Log
public class MysqlPersistenceProvider implements PersistenceProvider {
    @Inject
    private VoxelGamesLib voxelGamesLib;
    @Inject
    private GlobalConfig config;

    @Override
    public void start() {
        // commented out since VGL autoloads this regardless
        //DB.initialize(voxelGamesLib, config);

        initialise();
    }

    @Override
    public void stop() {
        //DB.close();
    }

    @Override
    public void saveUser(@Nonnull User user) {
        DB.executeUpdateAsync("UPDATE vgl_user SET username = ? WHERE uuid = ?", user.getRawDisplayName(), user.getUuid());
    }

    @Override
    @Nonnull
    public Optional<User> loadUser(@Nonnull UUID id) {
        User user = new GamePlayer();
        DbRow userData;

        try {
            userData = DB.getFirstRow("SELECT * FROM vgl_user WHERE uuid = ?", id);
        } catch (SQLException e) {
            log.severe("Error loading player data for UUID: " + id);
            e.printStackTrace();
            return Optional.empty();
        }

        user.setUuid(userData.get("uuid"));
        user.setDisplayName(userData.get("username"));
        // todo: set more variables

        return Optional.ofNullable(user);
    }

    private void initialise() {
        // todo: create tables if they don't exist
    }
}
