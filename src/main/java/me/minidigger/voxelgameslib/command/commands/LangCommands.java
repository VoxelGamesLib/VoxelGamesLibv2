package me.minidigger.voxelgameslib.command.commands;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangHandler;
import me.minidigger.voxelgameslib.lang.LangKey;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.persistence.PersistenceHandler;

/**
 * Handles all commands related to lang and i18n
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class LangCommands {

  @Inject
  private LangHandler langHandler;
  @Inject
  private GlobalConfig globalConfig;
  @Inject
  private PersistenceHandler persistenceHandler;

  public void lang(@Nonnull CommandArguments args) {
    StringBuilder sb = new StringBuilder();
    for (Locale loc : langHandler.getInstalledLocales()) {
      sb.append(loc.getTag()).append(" (").append(loc.getName()).append("), ");
    }
    sb.setLength(sb.length() - 1);
    Lang.msg(args.getSender(), LangKey.LANG_INSTALLED, sb.toString());
    Lang.msg(args.getSender(), LangKey.LANG_CURRENT,
        args.getSender().getData().getLocale().getName());
  }

  public void set(@Nonnull CommandArguments args) {
    Optional<Locale> loc = Locale.fromTag(args.getArg(0));
    if (!loc.isPresent()) {
      loc = Locale.fromName(args.getArg(0));
      if (!loc.isPresent()) {
        Lang.msg(args.getSender(), LangKey.LANG_UNKNOWN, args.getArg(0));
        return;
      }
    }
    args.getSender().getData().setLocale(loc.get());
    Lang.msg(args.getSender(), LangKey.LANG_UPDATE, loc.get().getName());
    if (!langHandler.getInstalledLocales().contains(loc.get())) {
      Lang.msg(args.getSender(), LangKey.LANG_NOT_ENABLED, loc.get().getName());
    }

    persistenceHandler.getProvider().saveUserData(args.getSender().getData());
  }
}
