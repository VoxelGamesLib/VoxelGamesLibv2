package me.minidigger.voxelgameslib.role;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;

/**
 * Handles all commands related to roles
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class RoleCommands {

  @Inject
  private RoleHandler roleHandler;
  @Inject
  private UserHandler userHandler;
  @Inject
  private PersistenceHandler persistenceHandler;

  public void role(CommandArguments arguments) {
    if (arguments.getNumArgs() < 1) {
      Lang.msg(arguments.getSender(), LangKey.ROLE_SELF,
          arguments.getSender().getData().getRole().getName());
      return;
    }

    Optional<User> user = userHandler.getUser(arguments.getArg(0));
    if (user.isPresent()) {
      Lang.msg(arguments.getSender(), LangKey.ROLE_OTHERS, user.get().getDisplayName(),
          user.get().getData().getRole().getName());
    } else {
      Lang.msg(arguments.getSender(), LangKey.GENERAL_INVALID_USER_NAME, arguments.getArg(0));
    }
  }

  public void set(CommandArguments arguments) {
    Optional<Role> role = roleHandler.getRole(arguments.getArg(1));
    if (!role.isPresent()) {
      Lang.msg(arguments.getSender(), LangKey.ROLE_UNKNOWN_ROLE, arguments.getArg(1));
      return;
    }

    if (arguments.getNumArgs() == 2) {
      Optional<User> user = userHandler.getUser(arguments.getArg(0));
      if (!user.isPresent()) {
        Lang.msg(arguments.getSender(), LangKey.GENERAL_INVALID_USER_NAME, arguments.getArg(0));
        return;
      }

      user.get().getData().setRole(role.get());
      Lang.msg(arguments.getSender(), LangKey.ROLE_UPDATED_OTHER,
          user.get().getData().getDisplayName(), role.get().getName());
      persistenceHandler.getProvider().saveUserData(user.get().getData());
    } else {
      arguments.getSender().getData().setRole(role.get());
      Lang.msg(arguments.getSender(), LangKey.ROLE_UPDATED_SELF, role.get().getName());
      persistenceHandler.getProvider().saveUserData(arguments.getSender().getData());
    }
  }
}
