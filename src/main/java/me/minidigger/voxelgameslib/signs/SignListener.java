package me.minidigger.voxelgameslib.signs;

import java.util.Optional;
import javax.inject.Inject;
import lombok.extern.java.Log;
import me.minidigger.voxelgameslib.event.events.sign.SignUpdateEvent;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.role.Permission;
import me.minidigger.voxelgameslib.role.Role;

@Log
@SuppressWarnings({"JavaDoc", "Duplicates"})
public class SignListener {

  private Permission placeHolderSignPlace = Permission
      .register("sign.placeholder.place", Role.MODERATOR);
  private Permission placeHolderSignBreak = Permission
      .register("sign.placeholder.break", Role.MODERATOR);
  private Permission buttonSignPlace = Permission.register("sign.button.place", Role.MODERATOR);
  private Permission buttonSignBreak = Permission.register("sign.button.break", Role.MODERATOR);

  @Inject
  private SignHandler signHandler;
  @Inject
  private Server server;

  public void signUpdate(SignUpdateEvent event) {
    Optional<SignLocation> sign = signHandler.getSignAt(event.getLocation(), event.getWorld());
    // this isn't a handled sign and it isn't a new sign
    if (!sign.isPresent()) {
      if (event.getUser().isPresent()) {
        // might be a new sign!
        sign = Optional
            .of(new SignLocation(event.getLocation(), event.getWorld(), server, event.getText()));
        assert sign.isPresent();
      } else {
        return;
      }
    }
    for (int i = 0; i < sign.get().getLines().length; i++) {
      String line = sign.get().getLines()[i];
      for (String key : signHandler.getPlaceHolders().keySet()) {
        if (line.contains("%" + key + "%")) {
          // we got a sign with a placeholder, first check if a user placed it and if he is allowed to do that
          if (event.getUser().isPresent()) {
            if (!event.getUser().get().hasPermission(placeHolderSignPlace)) {
              Lang.msg(event.getUser().get(), LangKey.SIGNS_PLACE_NO_PERM, key,
                  placeHolderSignPlace.getRole().getName());
              event.setCanceled(true);
              return;
            } else {
              Lang.msg(event.getUser().get(), LangKey.SIGNS_PLACE_SUCCESS, key);
              signHandler.addSign(event.getLocation(), event.getWorld(), event.getText());
            }
          }

          SignPlaceHolder placeHolder = signHandler.getPlaceHolders().get(key);
          if (placeHolder instanceof SimpleSignPlaceHolder) {
            line = line
                .replace("%" + key + "%", ((SimpleSignPlaceHolder) placeHolder).apply(event, key));
          } else if (placeHolder instanceof FullSignPlaceHolder) {
            event.setText(((FullSignPlaceHolder) placeHolder).apply(event, key));
            return;
          } else {
            log.warning(
                "Unknown SignPlaceHolder type " + placeHolder.getClass().getName() + " with key "
                    + key);
          }
        }
      }

      // this is a new sign, has it a sign button?
      if (event.getUser().isPresent()) {
        for (String key : signHandler.getButtons().keySet()) {
          if (line.contains("%" + key + "%")) {
            if (!event.getUser().get().hasPermission(buttonSignPlace)) {
              Lang.msg(event.getUser().get(), LangKey.SIGNS_PLACE_NO_PERM, key,
                  buttonSignBreak.getRole().getName());
              event.setCanceled(true);
              return;
            } else {
              Lang.msg(event.getUser().get(), LangKey.SIGNS_PLACE_SUCCESS, key);
              // we don't need to worry here if the text has been modified since it never can override the real text
              signHandler.addSign(event.getLocation(), event.getWorld(), event.getText());
            }
          }
        }
      }

      event.getText()[i] = line;
    }
  }

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
            if (event.getUser().hasPermission(buttonSignBreak)) {
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

  public void signInteract(UserInteractEvent event) {
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
