package com.voxelgameslib.voxelgameslib.persistence;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.user.GamePlayer;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.utils.db.DB;
import com.voxelgameslib.voxelgameslib.utils.db.DbRow;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Log
public class MysqlPersistenceProvider implements PersistenceProvider {
    @Inject
    private VoxelGamesLib voxelGamesLib;

    @Override
    public void start() {
        DB.initialize(voxelGamesLib);

        initialise();
    }

    @Override
    public void stop() {
        DB.close();
    }

    @Override
    public void saveUser(User user) {
        DB.executeUpdateAsync("UPDATE vgl_user SET username = ? WHERE uuid = ?", user.getRawDisplayName(), user.getUuid());
    }

    @Override
    public Optional<User> loadUser(UUID id) {
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
