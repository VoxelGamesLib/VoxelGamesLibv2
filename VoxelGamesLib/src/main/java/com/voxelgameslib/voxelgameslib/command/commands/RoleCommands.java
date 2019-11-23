package com.voxelgameslib.voxelgameslib.command.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.internal.lang.Lang;
import com.voxelgameslib.voxelgameslib.internal.lang.LangKey;
import com.voxelgameslib.voxelgameslib.internal.persistence.PersistenceHandler;
import com.voxelgameslib.voxelgameslib.api.role.Role;
import com.voxelgameslib.voxelgameslib.components.user.User;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;

/**
 * Handles all commands related to roles
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
@CommandAlias("role")
public class RoleCommands extends BaseCommand {

    @Inject
    private PersistenceHandler persistenceHandler;

    @HelpCommand
    @CommandPermission("%user")
    public void doHelp(@Nonnull User sender, @Nonnull CommandHelp help) {
        Lang.msg(sender, LangKey.ROLE_SELF, sender.getRole().getName());
        help.showHelp();
    }

    @CommandPermission("%moderator")
    @Syntax("[user] - the user which role you wanna get")
    public void role(@Nonnull User sender, @Nullable @co.aikar.commands.annotation.Optional User user) {
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
    public void set(@Nonnull User sender, @Nonnull User user, @Nonnull Role role) {
        user.setRole(role);
        user.applyRolePrefix();
        user.applyRoleSuffix();
        Lang.msg(sender, LangKey.ROLE_UPDATED_OTHER,
                user.getDisplayName(), role.getName());
        persistenceHandler.getProvider().saveUser(user.getUserData());
    }
}
