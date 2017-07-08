package me.minidigger.voxelgameslib.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;
import me.minidigger.voxelgameslib.role.Role;
import me.minidigger.voxelgameslib.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.UnknownHandler;

/**
 * Handles all commands related to roles
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("role")
public class RoleCommands extends BaseCommand {

    @Inject
    private PersistenceHandler persistenceHandler;

    @Default
    @CommandPermission("%user")
    public void role(User sender) {
        Lang.msg(sender, LangKey.ROLE_SELF, sender.getRole().getName());
    }

    @UnknownHandler
    @CommandPermission("%moderator")
    @Syntax("[user] - the user which role you wanna get")
    public void role(User sender, @co.aikar.commands.annotation.Optional User user) {
        if (user == null) {
            Lang.msg(sender, LangKey.ROLE_SELF, sender.getRole().getName());
            return;
        }

        Lang.msg(sender, LangKey.ROLE_OTHERS, user.getDisplayName(), user.getRole().getName());
    }

    @Subcommand("set")
    @Syntax("<user> - the user which role should be changed\n"
            + "<role> - the new role")
    @CommandPermission("%admin")
    @CommandCompletion("@players @roles")
    public void set(User sender, User user, Role role) {
        user.setRole(role);
        user.applyRolePrefix();
        user.applyRoleSuffix();
        Lang.msg(sender, LangKey.ROLE_UPDATED_OTHER,
                user.getDisplayName(), role.getName());
        persistenceHandler.getProvider().saveUser(user);
    }
}
