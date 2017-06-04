package me.minidigger.voxelgameslib.world;

import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.minidigger.voxelgameslib.lang.Lang;
import me.minidigger.voxelgameslib.lang.LangKey;
import org.bukkit.Material;

/**
 * Commands related to the edit mode
 */
@Singleton
@SuppressWarnings("JavaDoc") // commands don't need javadoc, go read the command's descriptions
public class EditMode{

  @Inject
  private Injector injector;

  @Nonnull
  private List<UUID> editMode = new ArrayList<>();

  public void editmode(@Nonnull CommandArguments args) {
    //TODO info about edit mode
  }

  public void on(@Nonnull CommandArguments args) {
    if (!editMode.contains(args.getSender().getUuid())) {
      editMode.add(args.getSender().getUuid());
      Lang.msg(args.getSender(), LangKey.EDITMODE_ENABLED);
    } else {
      Lang.msg(args.getSender(), LangKey.EDITMODE_ALREADY_ENABLED);
    }
  }

  public void off(@Nonnull CommandArguments args) {
    if (editMode.contains(args.getSender().getUuid())) {
      editMode.remove(args.getSender().getUuid());
      Lang.msg(args.getSender(), LangKey.EDITMODE_DISABLED);
    } else {
      Lang.msg(args.getSender(), LangKey.EDITMODE_NOT_ENABLED);
    }
  }

  public void skull(@Nonnull CommandArguments args) {
    if (editMode.contains(args.getSender().getUuid())) {
      String name = args.getArg(0);
      Item skull = new ItemBuilder(Material.SKULL_ITEM, injector).variation((byte) 3).name(name)
          .meta(m -> ((SkullItemMetaData) m).setOwner(name)).build();
      args.getSender().setItemInHand(Hand.MAINHAND, skull);
    } else {
      Lang.msg(args.getSender(), LangKey.EDITMODE_NOT_ENABLED);
    }
  }

  public void chest(@Nonnull CommandArguments args) {
    if (editMode.contains(args.getSender().getUuid())) {
      String name = args.getArg(0);
      Item chest = new ItemBuilder(Material.CHEST, injector).name(name).build();
      args.getSender().setItemInHand(Hand.MAINHAND, chest);
    } else {
      Lang.msg(args.getSender(), LangKey.EDITMODE_NOT_ENABLED);
    }
  }
}
