package me.minidigger.voxelgameslib.signs;

import java.util.Optional;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.role.Permission;
import me.minidigger.voxelgameslib.role.Role;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

  @EventHandler
  public void signUpdate(SignChangeEvent event) {
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
      for (String key : signHandler.getPlaceHolders().keySet()) {
        if (line.contains("%" + key + "%")) {
          // we got a sign with a placeholder, first check if a user placed it and if he is allowed to do that
          if (event.getPlayer() != null) {
            if (!event.getPlayer().hasPermission(placeHolderSignPlace)) {
              Lang.msg(event.getPlayer(), LangKey.SIGNS_PLACE_NO_PERM, key,
                  placeHolderSignPlace.getRole().getName());
              event.setCanceled(true);
              return;
            } else {
              Lang.msg(event.getPlayer(), LangKey.SIGNS_PLACE_SUCCESS, key);
              signHandler.addSign(event.getBlock().getLocation(), event.getLines());
            }
          }

          SignPlaceHolder placeHolder = signHandler.getPlaceHolders().get(key);
          if (placeHolder instanceof SimpleSignPlaceHolder) {
            line = line
                .replace("%" + key + "%", ((SimpleSignPlaceHolder) placeHolder).apply(event, key));
          } else if (placeHolder instanceof FullSignPlaceHolder) {
            event.setLines(((FullSignPlaceHolder) placeHolder).apply(event, key));
            return;
          } else {
            log.warning(
                "Unknown SignPlaceHolder type " + placeHolder.getClass().getName() + " with key "
                    + key);
          }
        }
      }

      // this is a new sign, has it a sign button?
      if (event.getPlayer() != null){
        for (String key : signHandler.getButtons().keySet()) {
          if (line.contains("%" + key + "%")) {
            if (!event.getPlayer().hasPermission(buttonSignPlace)) {
              Lang.msg(event.getPlayer(), LangKey.SIGNS_PLACE_NO_PERM, key,
                  buttonSignBreak.getRole().getName());
              event.setCanceled(true);
              return;
            } else {
              Lang.msg(event.getPlayer(), LangKey.SIGNS_PLACE_SUCCESS, key);
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
    if (event.getBlock().getMetaData() instanceof SignMetaData) {
      // has sign a placeholder?
      Optional<SignLocation> sign = signHandler
          .getSignAt(event.getBlock().getLocation(), event.getBlock().getWorld());
      if (!sign.isPresent()) {
        return;
      }
      //TODO also check sign buttons here
      for (int i = 0; i < sign.get().getLines().length; i++) {
        String line = sign.get().getLines()[i];
        for (String key : signHandler.getPlaceHolders().keySet()) {
          if (line.contains("%" + key + "%")) {
            // has user permission for that?
            if (event.getUser().hasPermission(placeHolderSignBreak)) {
              Lang.msg(event.getUser(), LangKey.SIGNS_BREAK_SUCCESS, key);
              signHandler.removeSign(event.getBlock());
              event.getBlock().setMaterial(Material.AIR);// to force a refresh of the block meta....
              return;
            } else {
              event.setCanceled(true);
              Lang.msg(event.getUser(), LangKey.SIGNS_BREAK_NO_PERM, key,
                  placeHolderSignBreak.getRole().getName());
              return;
            }
          }
        }

        for (String key : signHandler.getButtons().keySet()) {
          if (line.contains("%" + key + "%")) {
            // has user permission for that?
            if (event.getPlayer().hasPermission(buttonSignBreak)) {
              Lang.msg(event.getUser(), LangKey.SIGNS_BREAK_SUCCESS, key);
              signHandler.removeSign(event.getBlock());
              event.getBlock().setMaterial(Material.AIR);// to force a refresh of the block meta....
              return;
            } else {
              event.setCanceled(true);
              Lang.msg(event.getUser(), LangKey.SIGNS_BREAK_NO_PERM, key,
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
    if (event.getType() != UserInteractEvent.Type.RIGHT_CLICK_BLOCK || event.getBlock() == null) {
      return;
    }
    // is block a sign?
    if (event.getBlock().getMetaData() instanceof SignMetaData) {
      Optional<SignLocation> sign = signHandler
          .getSignAt(event.getBlock().getLocation(), event.getBlock().getWorld());
      if (!sign.isPresent()) {
        return;
      }
      for (int i = 0; i < sign.get().getLines().length; i++) {
        String line = sign.get().getLines()[i];
        for (String key : signHandler.getButtons().keySet()) {
          if (line.contains("%" + key + "%")) {
            //TODO implement perm check
            signHandler.getButtons().get(key).execute(event.getUser(), event.getBlock());
          }
        }
      }
    }

  }
}
