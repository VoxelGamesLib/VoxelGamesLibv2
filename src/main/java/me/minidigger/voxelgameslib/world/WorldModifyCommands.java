package me.minidigger.voxelgameslib.world;

import com.google.inject.Singleton;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.Optional;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.exception.WorldException;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.map.Map;
import me.minidigger.voxelgameslib.map.Vector3D;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.utils.FileUtils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.extern.java.Log;

@Log
@Singleton
@CommandAlias("worldcreator|wc")
@Subcommand("modify|m")
public class WorldModifyCommands extends BaseCommand {

    @Inject
    private WorldHandler worldHandler;

    private User editor;
    private Map map;

    @CommandAlias("modify|help")
    @CommandPermission("%admin")
    public void modify(User sender) {
        Lang.msg(sender, LangKey.WORLD_MODIFY_HELP);
    }

    @Subcommand("start")
    @CommandPermission("%admin")
    @Syntax("<world> - the name of the map you want to modify")
    public void start(User user, String world) {
        if (editor != null) {
            Lang.msg(user, LangKey.WORLD_CREATOR_IN_USE,
                    editor.getDisplayName());
            return;
        }
        editor = user;

        // load data
        Optional<Map> o = worldHandler.getMap(world);
        this.map = o.orElseGet(() -> worldHandler.loadMap(world));

        // load world
        map.load(editor.getUuid(), map.getWorldName());

        File file = new File(worldHandler.getWorldContainer(), map.getLoadedName(editor.getUuid()));

        try {
            ZipFile zip = new ZipFile(new File(worldHandler.getWorldsFolder(), map.getWorldName() + ".zip"));
            zip.extractAll(file.getAbsolutePath());
            FileUtils.delete(new File(file, "uid.dat"));
        } catch (ZipException e) {
            throw new WorldException("Could not unzip world " + map.getInfo().getName() + ".", e);
        }

        worldHandler.loadLocalWorld(map.getLoadedName(editor.getUuid()));

        // tp
        user.getPlayer().teleport(map.getCenter().toLocation(map.getLoadedName(user.getUuid())));

        Lang.msg(user, LangKey.WORLD_MODIFY_START);
        //TODO use inventory for world creator
    }

    @Subcommand("displayname")
    @CommandPermission("%admin")
    @Syntax("[name] - the new display name of the map")
    public void displayname(User user, @co.aikar.commands.annotation.Optional String name) {
        if (!check(user)) return;
        // view
        if (name == null) {
            Lang.msg(user, LangKey.WORLD_MODIFY_DISPLAYNAME_VIEW, map.getInfo().getName());
        }
        // edit
        else {
            map.getInfo().setName(name);
            Lang.msg(user, LangKey.WORLD_MODIFY_DISPLAYNAME_EDIT, name);
        }
    }

    @Subcommand("author")
    @CommandPermission("%admin")
    @Syntax("[author] - the new author of the map")
    public void author(User user, @co.aikar.commands.annotation.Optional String author) {
        if (!check(user)) return;
        // view
        if (author == null) {
            Lang.msg(user, LangKey.WORLD_MODIFY_AUTHOR_VIEW, map.getInfo().getAuthor());
        }
        // edit
        else {
            map.getInfo().setAuthor(author);
            Lang.msg(user, LangKey.WORLD_MODIFY_AUTHOR_EDIT, author);
        }
    }

    @Subcommand("radius")
    @CommandPermission("%admin")
    @Syntax("<radius> - the radius of the map")
    public void radius(User user, @co.aikar.commands.annotation.Optional Integer radius) {
        if (!check(user)) return;
        // view
        if (radius == null) {
            Lang.msg(user, LangKey.WORLD_MODIFY_RADIUS_VIEW, map.getRadius());
        }
        // edit
        else {
            map.setRadius(radius);
            Lang.msg(user, LangKey.WORLD_MODIFY_RADIUS_EDIT, radius);
        }
    }

    @Subcommand("center")
    @CommandPermission("%admin")
    public void center(User user, String set) {
        if (!check(user)) return;
        // view
        if (set == null) {
            Lang.msg(user, LangKey.WORLD_MODIFY_CENTER_VIEW, map.getCenter());
        }
        // edit
        else {
            map.setCenter(new Vector3D(user.getPlayer().getLocation().getX(), user.getPlayer().getLocation().getY(), user.getPlayer().getLocation().getZ()));
            Lang.msg(user, LangKey.WORLD_MODIFY_CENTER_EDIT, map.getCenter());
        }
    }

    @Subcommand("gamemode")
    @CommandPermission("%admin")
    @Syntax("<gamemode> - the gamesmodes")
    public void gamemode(User user, @co.aikar.commands.annotation.Optional String gamemode) {
        if (!check(user)) return;
        // remove all
        // add one
        // save
        //TODO lets do this later
    }

    @Subcommand("stop")
    @CommandPermission("%admin")
    public void stop(User user) {
        if (!check(user)) return;
        worldHandler.finishWorldEditing(editor, map);

        editor = null;
        map = null;
    }

    private boolean check(User user) {
        if (editor == null) {
            Lang.msg(user, LangKey.WORLD_MODIFY_NOT_STARTED);
            return false;
        }
        if (!editor.getUuid().equals(user.getUuid())) {
            Lang.msg(user, LangKey.WORLD_CREATOR_IN_USE, editor.getDisplayName());
            return false;
        }
        return true;
    }
}
