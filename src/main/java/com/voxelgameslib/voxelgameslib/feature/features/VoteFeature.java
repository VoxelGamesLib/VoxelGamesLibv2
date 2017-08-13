package com.voxelgameslib.voxelgameslib.feature.features;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.VoxelGamesLib;
import com.voxelgameslib.voxelgameslib.components.inventory.BasicInventory;
import com.voxelgameslib.voxelgameslib.components.inventory.InventoryHandler;
import com.voxelgameslib.voxelgameslib.event.GameEvent;
import com.voxelgameslib.voxelgameslib.event.events.game.GameJoinEvent;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeature;
import com.voxelgameslib.voxelgameslib.feature.AbstractFeatureCommand;
import com.voxelgameslib.voxelgameslib.feature.FeatureCommandImplementor;
import com.voxelgameslib.voxelgameslib.feature.FeatureInfo;
import com.voxelgameslib.voxelgameslib.game.DefaultGameData;
import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.map.MapInfo;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.user.UserHandler;
import com.voxelgameslib.voxelgameslib.utils.ItemBuilder;
import com.voxelgameslib.voxelgameslib.world.WorldConfig;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;

@FeatureInfo(name = "VoteFeature", author = "MiniDigger", version = "1.0",
        description = "Allow players to vote on maps")
@Log
public class VoteFeature extends AbstractFeature implements FeatureCommandImplementor {

    //TODO add scoreboard
    @Getter
    @Setter
    @Expose
    private int maxMaps = 3;

    @Inject
    private WorldConfig config;
    @Inject
    private UserHandler userHandler;
    @Inject
    private InventoryHandler inventoryHandler;
    @Inject
    private VoxelGamesLib voxelGamesLib;

    private Map<UUID, Integer> votes = new HashMap<>();
    private Map<Integer, MapInfo> availableMaps = new HashMap<>();

    @Getter
    @Setter
    @Expose
    private boolean enableVoteMenu = true;
    @Getter
    @Setter
    //@Expose TODO figure out how we want to expose items
    private ItemStack openMenuItem = new ItemBuilder(Material.PAPER).amount(1).name(ChatColor.GOLD + "Vote for a map").build();

    @Override
    public void start() {
        String mode = getPhase().getGame().getGameMode().getName();
        int id = 1;
        for (MapInfo info : config.maps) {
            if (info.getGamemodes().contains(mode)) {
                availableMaps.put(id++, info);
            }

            if (id == maxMaps - 1) {
                break;
            }
        }

        if (availableMaps.size() == 0) {
            getPhase().getGame().broadcastMessage(LangKey.VOTE_NO_MAPS_FOUND);
            getPhase().getGame().abortGame();
            log.warning("Game " + getPhase().getGame().getUuid() + "(" + getPhase().getGame().getGameMode().getName() + ")" +
                    " was aborted because it didn't find any maps to play!");
            return;
        }

        getPhase().getGame().getPlayers().forEach(this::sendVoteMessage);
        if (enableVoteMenu) {
            getPhase().getGame().getPlayers().forEach(this::giveVoteMenuItem);
        }
    }

    @Override
    public void stop() {
        Map<Integer, Integer> votes = new HashMap<>();
        int max = -1;
        int maxMap = -1;
        for (Integer map : this.votes.values()) {
            int old = votes.getOrDefault(map, 0);
            old++;
            if (old > max) {
                max = old;
                maxMap = map;
            }
            votes.put(map, old);
        }

        MapInfo winner = availableMaps.get(maxMap);
        if (winner == null) {
            // use first map if nobody won
            winner = availableMaps.values().iterator().next();
        }

        DefaultGameData gameData = getPhase().getGame().getGameData(DefaultGameData.class).orElse(new DefaultGameData());
        gameData.voteWinner = winner;
        getPhase().getGame().putGameData(gameData);
        getPhase().getGame().broadcastMessage(LangKey.VOTE_END, winner.getName(), winner.getAuthor(), Math.max(max, 0));
    }

    @Override
    @Nonnull
    public Class<? extends AbstractFeatureCommand> getCommandClass() {
        return VoteFeatureCommand.class;
    }

    /**
     * Sends the vote message to that user
     *
     * @param user the user that should receive the message
     */
    public void sendVoteMessage(@Nonnull User user) {
        Lang.msg(user, LangKey.VOTE_MESSAGE_TOP);
        for (int id : availableMaps.keySet()) {
            MapInfo info = availableMaps.get(id);
            Lang.msg(user, LangKey.VOTE_MESSAGE_MAP, "/vote " + id, id, info.getName(), info.getAuthor());
        }
        Lang.msg(user, LangKey.VOTE_MESSAGE_BOT);
    }

    public void giveVoteMenuItem(@Nonnull User user) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (!getPhase().isRunning()) return;
                user.getPlayer().getInventory().setItem(0, openMenuItem);
                user.getPlayer().updateInventory();
            }
        }.runTaskLater(voxelGamesLib, 5); // delay because we might be in progress of switching worlds
    }

    @GameEvent
    public void onJoin(@Nonnull GameJoinEvent event) {
        sendVoteMessage(event.getUser());
        if (enableVoteMenu) {
            giveVoteMenuItem(event.getUser());
        }
    }

    @GameEvent
    public void openVoteMenu(@Nonnull PlayerInteractEvent event, User user) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && openMenuItem.equals(event.getItem())) {
            int pos = 0;
            BasicInventory basicInventory = inventoryHandler.createInventory(BasicInventory.class, user, "Vote for a map", availableMaps.size());
            for (int id : availableMaps.keySet()) {
                MapInfo info = availableMaps.get(id);
                ItemStack item = new ItemBuilder(Material.PAPER).amount(id).name(info.getName()).lore(info.getAuthor()).build();
                basicInventory.getBukkitInventory().setItem(pos++, item);
                basicInventory.addClickAction(item, ((itemStack, inventoryClickEvent) -> {
                    confirmVote(user, id);
                    basicInventory.close();

                    // Destroy the inventory, we don't need it anymore
                    inventoryHandler.removeInventory(basicInventory.getIdentifier());
                }));
            }
            user.getPlayer().openInventory(basicInventory.getBukkitInventory());
        }
    }

    /**
     * Confirms a vote for a map
     */
    private void confirmVote(@Nonnull User voter, @Nonnull Integer mapId) {
        if (votes.containsKey(voter.getUuid())) {
            Lang.msg(voter, LangKey.VOTE_ALREADY_VOTED);
        } else {
            MapInfo mapInfo = availableMaps.get(mapId);
            if (mapInfo == null) {
                Lang.msg(voter, LangKey.VOTE_UNKNOWN_MAP, mapId);
                return;
            }

            votes.put(voter.getUuid(), mapId);
            Lang.msg(voter, LangKey.VOTE_SUBMITTED, mapInfo.getName(), mapId);
        }
    }

    @Singleton
    static class VoteFeatureCommand extends AbstractFeatureCommand<VoteFeature> {

        @CommandAlias("vote")
        @CommandPermission("%user")
        @Syntax("[map] - the map to vote for")
        public void vote(@Nonnull User sender, @Nullable @Optional Integer map) {
            if (map == null) {
                getFeature().sendVoteMessage(sender);
            } else {
                getFeature().confirmVote(sender, map);
            }
        }
    }
}
