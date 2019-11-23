package com.voxelgameslib.voxelgameslib.internal.texture;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.api.exception.VoxelGameLibException;

import org.bukkit.Bukkit;

@Singleton
public class PlayerProfileTypeAdapter implements JsonDeserializer<PlayerProfile>, JsonSerializer<PlayerProfile> {

    @Override
    public PlayerProfile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Dummy dummy = jsonDeserializationContext.deserialize(jsonElement, Dummy.class);
        PlayerProfile playerProfile;
        if (dummy.id != null) {
            playerProfile = Bukkit.createProfile(dummy.id);
        } else if (dummy.name != null) {
            playerProfile = Bukkit.createProfile(dummy.name);
        } else {
            throw new VoxelGameLibException("Could not parse player profile! " + jsonElement);
        }
        playerProfile.setProperties(dummy.properties);
        playerProfile.setId(dummy.id);
        playerProfile.setName(dummy.name);
        return playerProfile;
    }

    @Override
    public JsonElement serialize(PlayerProfile playerProfile, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(new Dummy(playerProfile.getId(), playerProfile.getName(), playerProfile.getProperties()));
    }

    class Dummy {
        private UUID id;
        private String name;
        private Set<ProfileProperty> properties;

        public Dummy(@Nullable UUID id, @Nullable String name, Set<ProfileProperty> properties) {
            this.id = id;
            this.name = name;
            this.properties = properties;
        }
    }
}
