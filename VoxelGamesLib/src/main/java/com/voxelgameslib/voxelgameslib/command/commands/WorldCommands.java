package com.voxelgameslib.voxelgameslib.command.commands;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.lang.Lang;
import com.voxelgameslib.voxelgameslib.lang.LangKey;
import com.voxelgameslib.voxelgameslib.map.Map;
import com.voxelgameslib.voxelgameslib.user.User;
import com.voxelgameslib.voxelgameslib.world.WorldHandler;

import org.bukkit.Bukkit;
import org.bukkit.World;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

//import co.aikar.commands.annotation.Description;

/**
 * Commands related to worlds
 */
@Singleton
@CommandAlias("world")
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class WorldCommands extends BaseCommand {

    //TODO world command completer

    @Inject
    private WorldHandler handler;

    @HelpCommand
    @CommandPermission("%admin")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("info")
    @CommandPermission("%admin")
    @Description("Shows some basic info about your current world")
    public void worldInfo(@Nonnull User sender) {
        Lang.msg(sender, LangKey.WORLD_INFO, sender.getPlayer().getLocation().getWorld().getName());
    }

    @Subcommand("load")
    @CommandPermission("%admin")
    @Syntax("<mapname> - the name of the map to load")
    @Description("Loads a map onto the server")
    public void load(@Nonnull User sender, @Nonnull String mapName) {
        Optional<Map> o = handler.getMap(mapName);
        Map map = o.orElseGet(() -> handler.loadMap(mapName));

        handler.loadWorld(map, sender.getUuid(), false);
    }

    @Subcommand("unload")
    @CommandPermission("%admin")
    @Syntax("<mapname> - the name of the map to unload")
    @Description("Unloads a map from the server")
    public void unload(@Nonnull User sender, @Nonnull String mapName) {
        Optional<Map> o = handler.getMap(mapName);
        if (o.isPresent()) {
            handler.unloadWorld(o.get(), sender.getUuid());
        } else {
            Lang.msg(sender, LangKey.WORLD_UNKNOWN_MAP, mapName);
        }
    }

    @Subcommand("loadlocal")
    @CommandPermission("%admin")
    @Syntax("<world> - the name of the world to load")
    @Description("Loads a local world onto the server")
    public void loadLocal(@Nonnull User sender, @Nonnull String world) {
        handler.loadLocalWorld(world);
    }

    @Subcommand("unloadlocal")
    @CommandPermission("%admin")
    @Syntax("<world> - the name of the world to unload")
    @Description("Unloads a local world from the server")
    public void unloadLocal(@Nonnull User sender, @Nonnull String world) {
        handler.unloadLocalWorld(world);
    }

    @Subcommand("tp")
    @CommandPermission("%admin")
    @Syntax("<world> - the name of the world to tp to")
    @Description("Teleports you to a world")
    public void tp(@Nonnull User sender, @Nonnull String world) {
        Optional<Map> o = handler.getMap(world);
        if (o.isPresent()) {
            Map map = o.get();
            sender.getPlayer().teleportAsync(map.getCenter().toLocation(map.getLoadedName(sender.getUuid())));
        } else {
            World w = Bukkit.getWorld(world);
            if (w != null) {
                sender.getPlayer().teleportAsync(Bukkit.getWorld(world).getSpawnLocation());
            }
            Lang.msg(sender, LangKey.WORLD_UNKNOWN_MAP, world);
        }
    }
}
