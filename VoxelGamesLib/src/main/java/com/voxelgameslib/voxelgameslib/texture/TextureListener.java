package com.voxelgameslib.voxelgameslib.texture;

import com.google.gson.Gson;

import com.destroystokyo.paper.event.player.PlayerHandshakeEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.properties.Property;

import org.mineskin.data.Skin;

import java.util.HashSet;
import javax.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TextureListener implements Listener {

    @Inject
    private TextureHandler textureHandler;

    @EventHandler
    public void onLogin(com.destroystokyo.paper.event.profile.LookupProfileEvent e) {
        doStuff(e.getPlayerProfile());
    }

    @EventHandler
    public void onLogin(com.destroystokyo.paper.event.profile.FillProfileEvent e) {
        doStuff(e.getPlayerProfile());
    }

    @EventHandler
    public void onLogin(com.destroystokyo.paper.event.profile.PreFillProfileEvent e) {
        doStuff(e.getPlayerProfile());
    }

    @EventHandler
    public void onLogin(com.destroystokyo.paper.event.profile.PreLookupProfileEvent e) {
        Skin skin = doStuff(Bukkit.createProfile(e.getUUID(), e.getName()));
        e.setProfileProperties(new HashSet<ProfileProperty>() {{
            add(new ProfileProperty("textures", skin.data.texture.value, skin.data.texture.signature));
        }});
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

    }

    @EventHandler
    public void onHandShake(PlayerHandshakeEvent e) {
        String texture = "eyJ0aW1lc3RhbXAiOjE1MTkyNTEzMjg5MjMsInByb2ZpbGVJZCI6IjA2OWE3OWY0NDRlOTQ3MjZhNWJlZmNhOTBlMzhhYWY1IiwicHJvZmlsZU5hbWUiOiJOb3RjaCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTExNmU2OWE4NDVlMjI3ZjdjYTFmZGRlOGMzNTdjOGM4MjFlYmQ0YmE2MTkzODJlYTRhMWY4N2Q0YWU5NCJ9LCJDQVBFIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVjM2NhYmZhZWVkNWRhZmU2MWM2NTQ2Mjk3ZTg1M2E1NDdjMzllYzIzOGQ3YzQ0YmY0ZWI0YTQ5ZGMxZjJjMCJ9fX0\\u003d";
        String sig = "BjKM+ZdrEl9nje8TvutxPwaFQVrgRSNQijiZledWY+OEl5g9aLkKglbneDnLA4csFqHEpSSh7rNJKZMWNcL0R+vMv0UyOd9CK4imt+gcLaV002zhjbeb6AtLQybaLecqCiVaZFZArRIOCZJYbC+wfuCmx88ExqHm+Oe28yqlSAI1hHLDMga2S7srbG9WnvtDaZAmVKaSWA4RgRQRJHoM3KCJh6k69hc5RglobXX8L8C0QlD5rv1KGXZC5rWq1O/8gYEUb79NT3UgDYG7hsqI1kf4gKvbUIBK6HsPfZA0AsUQ18FrYL/8JUyd0Kp6zrgOa4nfsIYaYoW1dSI5NM4pLroW6mEeXOK+Kz2CdHu/fvrd60yLJ/KcYeGFdt5i4cej2AlKOR58muJyuC81f5jMLO79kp/X1rw6r4+xui7o9hszs8HmiQKkTas3ih8DuWHFLnvFuQm5SoftjKfxr+1SpMHB8VCfdh0nGfCzRWP9ENm3mn0SNV2c+NLh/Oe+sbQ9Bf1i9lE7GhCVPIoH7RqsrIfRa50E+u5dFO42t16qMsYVTQK6/vPaeaAATOtdhgeWWtU/oupJgcoZQfkl+AUYm2K3C2WP1wBXpCCzHDf2Zn/x5Q7YkV4ecy4JRShvQHWWLkNzSHBJOPiYUIBlx3AafHVWQVZBXpJd7qNfM0H+r6c\\u003d";
        e.setPropertiesJson("[{\"name\":\"textures\",\"value\":\"" + texture + "\",\"signature\":\"" + sig + "\"}]");
        e.setSocketAddressHostname("localhost");
        e.setServerHostname("localhost");
        e.setCancelled(false);
        e.setFailed(false);
    }

    public Skin doStuff(PlayerProfile playerProfile) {
        Skin skin = textureHandler.getSkin(118300).orElseThrow(() -> new RuntimeException("d"));
        playerProfile.clearProperties();
        playerProfile.setProperty(new ProfileProperty("textures", skin.data.texture.value, skin.data.texture.signature));
        return skin;
    }
}
