package me.minidigger.voxelgameslib.signs;

import java.util.Optional;
import javax.inject.Inject;

import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.role.Permission;
import me.minidigger.voxelgameslib.role.Role;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import lombok.extern.java.Log;

@Log
@SuppressWarnings({"JavaDoc", "Duplicates"})
public class SignListener implements Listener {

    private Permission placeHolderSignPlace = Permission
            .register("sign.placeholder.place", Role.MODERATOR);
    private Permission placeHolderSignBreak = Permission
            .register("sign.placeholder.break", Role.MODERATOR);
    private Permission buttonSignPlace = Permission.register("sign.button.place", Role.MODERATOR);
    private Permission buttonSignBreak = Permission.register("sign.button.break", Role.MODERATOR);

    @Inject
    private SignHandler signHandler;
    @Inject
    private UserHandler userHandler;

    @EventHandler
    public void signUpdate(SignChangeEvent event) {
        User user = userHandler.getUser(event.getPlayer().getUniqueId())
                .orElseThrow(() -> new UserException(
                        "Unknown user " + event.getPlayer().getDisplayName() + "(" + event.getPlayer()
                                .getUniqueId() + ")"));
        Optional<SignLocation> sign = signHandler.getSignAt(event.getBlock().getLocation());
        // this isn't a handled sign and it isn't a new sign
        if (!sign.isPresent()) {
            if (event.getPlayer() != null) {
                sign = Optional.of(new SignLocation(event.getBlock().getLocation(), event.getLines()));
            }
            //TODO do we need another check here? check old source
        }
        for (int i = 0; i < sign.get().getLines().length; i++) {
            String line = sign.get().getLines()[i];
            for (String key : signHandler.getSignPlaceholders().getPlaceHolders().keySet()) {
                if (line.contains("%" + key + "%")) {
                    // we got a sign with a placeholder, first check if a user placed it and if he is allowed to do that
                    if (event.getPlayer() != null) {
                        if (!user.hasPermission(placeHolderSignPlace)) {
                            Lang.msg(user, LangKey.SIGNS_PLACE_NO_PERM, key,
                                    placeHolderSignPlace.getRole().getName());
                            event.setCancelled(true);
                            return;
                        } else {
                            Lang.msg(user, LangKey.SIGNS_PLACE_SUCCESS, key);
                            signHandler.addSign(event.getBlock().getLocation(), event.getLines());
                        }
                    }

                    SignPlaceHolder placeHolder = signHandler.getSignPlaceholders().getPlaceHolders().get(key);
                    if (placeHolder instanceof SimpleSignPlaceHolder) {
                        line = line
                                .replace("%" + key + "%", ((SimpleSignPlaceHolder) placeHolder).apply(event, key));
                    } else if (placeHolder instanceof FullSignPlaceHolder) {
                        String[] newlines = ((FullSignPlaceHolder) placeHolder).apply(event, key);
                        for (int j = 0; j < newlines.length;
                             j++) {
                            event.setLine(j, newlines[j]);
                        }
                        return;
                    } else {
                        log.warning(
                                "Unknown SignPlaceHolder type " + placeHolder.getClass().getName() + " with key "
                                        + key);
                    }
                }
            }

            // this is a new sign, has it a sign button?
            if (event.getPlayer() != null) {
                for (String key : signHandler.getSignButtons().getButtons().keySet()) {
                    if (line.contains("%" + key + "%")) {
                        if (!user.hasPermission(buttonSignPlace)) {
                            Lang.msg(user, LangKey.SIGNS_PLACE_NO_PERM, key, buttonSignBreak.getRole().getName());
                            event.setCancelled(true);
                            return;
                        } else {
                            Lang.msg(user, LangKey.SIGNS_PLACE_SUCCESS, key);
                            // we don't need to worry here if the text has been modified since it never can override the real text
                            signHandler.addSign(event.getBlock().getLocation(), event.getLines());
                        }
                    }
                }
            }

            event.getLines()[i] = line;
        }
    }

    @EventHandler
    public void signBreakEvent(BlockBreakEvent event) {
        // is block a sign?
        if (event.getBlock().getState() instanceof Sign) {
            // has sign a placeholder?
            Optional<SignLocation> sign = signHandler
                    .getSignAt(event.getBlock().getLocation());
            if (!sign.isPresent()) {
                return;
            }

            //TODO also check sign buttons here
            User user = userHandler.getUser(event.getPlayer().getUniqueId())
                    .orElseThrow(() -> new UserException(
                            "Unknown user " + event.getPlayer().getDisplayName() + "(" + event.getPlayer()
                                    .getUniqueId() + ")"));

            for (int i = 0; i < sign.get().getLines().length; i++) {
                String line = sign.get().getLines()[i];
                for (String key : signHandler.getSignPlaceholders().getPlaceHolders().keySet()) {
                    if (line.contains("%" + key + "%")) {
                        // has user permission for that?
                        if (user.hasPermission(placeHolderSignBreak)) {
                            Lang.msg(user, LangKey.SIGNS_BREAK_SUCCESS, key);
                            signHandler.removeSign(event.getBlock());
                            event.getBlock().setType(Material.AIR);
                            return;
                        } else {
                            event.setCancelled(true);
                            Lang.msg(user, LangKey.SIGNS_BREAK_NO_PERM, key,
                                    placeHolderSignBreak.getRole().getName());
                            return;
                        }
                    }
                }

                for (String key : signHandler.getSignButtons().getButtons().keySet()) {
                    if (line.contains("%" + key + "%")) {
                        // has user permission for that?
                        if (user.hasPermission(buttonSignBreak)) {
                            Lang.msg(user, LangKey.SIGNS_BREAK_SUCCESS, key);
                            signHandler.removeSign(event.getBlock());
                            event.getBlock().setType(Material.AIR);
                            return;
                        } else {
                            event.setCancelled(true);
                            Lang.msg(user, LangKey.SIGNS_BREAK_NO_PERM, key,
                                    buttonSignBreak.getRole().getName());
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void signInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }
        // is block a sign?
        if (event.getClickedBlock().getState() instanceof Sign) {
            Optional<SignLocation> sign = signHandler.getSignAt(event.getClickedBlock().getLocation());
            if (!sign.isPresent()) {
                return;
            }
            User user = userHandler.getUser(event.getPlayer().getUniqueId())
                    .orElseThrow(() -> new UserException(
                            "Unknown user " + event.getPlayer().getDisplayName() + "(" + event.getPlayer()
                                    .getUniqueId() + ")"));
            for (int i = 0; i < sign.get().getLines().length; i++) {
                String line = sign.get().getLines()[i];
                for (String key : signHandler.getSignButtons().getButtons().keySet()) {
                    if (line.contains("%" + key + "%")) {
                        //TODO implement perm check
                        signHandler.getSignButtons().getButtons().get(key).execute(user, event.getClickedBlock());
                    }
                }
            }
        }

    }
}
