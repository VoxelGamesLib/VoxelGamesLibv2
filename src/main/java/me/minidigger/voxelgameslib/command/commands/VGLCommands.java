package me.minidigger.voxelgameslib.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.google.inject.name.Named;
import javax.inject.Inject;
import org.bukkit.command.CommandSender;

@CommandAlias("vgl|voxelgameslib")
public final class VGLCommands extends BaseCommand {

  @Inject
  @Named("PluginVersion")
  private String pluginVersion;

  @Subcommand("version")
  @CommandPermission("%user")
  public void version(CommandSender sender) {
    sender.sendMessage("You are using VoxelGamesLib version " + pluginVersion);
  }
}
