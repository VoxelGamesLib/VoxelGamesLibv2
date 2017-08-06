package com.voxelgameslib.voxelgameslib.persistence;

import com.google.gson.Gson;

import com.voxelgameslib.voxelgameslib.user.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Simple persistence provider which uses gson to save the stuff as json to a flat file<br> <b>NOT
 * RECOMMENDED FOR PRODUCTION</b>
 */
public class FlatFileJsonPersistenceProvider implements PersistenceProvider {

    @Inject
    private Gson gson;

    @Inject
    @Named("DataFolder")
    private File folder;

    private File UserFile;
    private Map<UUID, User> UserMap;

    @SuppressWarnings("Duplicates")
    @Override
    public void start() {
        if (!folder.exists()) {
            folder.mkdir();
        }

        // users
        UserFile = new File(folder, "User.json");
        UserMap = new HashMap<>();

        if (!UserFile.exists()) {
            saveUsers();
        }

        try {
            String json = Files.readAllLines(UserFile.toPath()).stream()
                    .collect(Collectors.joining());
            //noinspection unchecked
            UserMap = gson.fromJson(json, Map.class);
            if (UserMap == null) {
                UserMap = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        UserMap.clear();
    }

    @Override
    public void saveUser(@Nonnull User user) {
        UserMap.put(user.getUuid(), user);
        saveUsers();
    }

    private void saveUsers() {
        try (FileWriter fw = new FileWriter(UserFile)) {
            fw.write(gson.toJson(UserMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nonnull
    public Optional<User> loadUser(@Nonnull UUID id) {
        return Optional.ofNullable(UserMap.get(id));
    }
}
