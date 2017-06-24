package me.minidigger.voxelgameslib.signs;

import net.kyori.text.TextComponent;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.minidigger.voxelgameslib.exception.UserException;
import me.minidigger.voxelgameslib.user.User;
import me.minidigger.voxelgameslib.user.UserHandler;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@Singleton
public class SignButtons implements Listener {

    @Inject
    private UserHandler userHandler;

    private Map<String, SignButton> buttons = new HashMap<>();

    /**
     * registers the default sign buttons
     */
    public void registerButtons() {
        registerButton("test", (user, block) -> user.sendMessage(TextComponent.of("WOW")));
    }

    /**
     * registers a new button
     *
     * @param key    the key to use
     * @param button the button that should be triggered
     */
    public void registerButton(String key, SignButton button) {
        buttons.put(key, button);
    }

    /**
     * gets map with all registered sign buttons
     *
     * @return all sign buttons
     */
    public Map<String, SignButton> getButtons() {
        return buttons;
    }


    @EventHandler
    public void signInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }
        // is block a sign?
        if (event.getClickedBlock().getState() instanceof Sign) {
            User user = userHandler.getUser(event.getPlayer().getUniqueId()).orElseThrow(() -> new UserException(
                    "Unknown user " + event.getPlayer().getDisplayName() + "(" + event.getPlayer().getUniqueId() + ")"));
            Sign sign = (Sign) event.getClickedBlock().getState();
            for (int i = 0; i < sign.getLines().length; i++) {
                String line = sign.getLines()[i];
                System.out.println(getButtons().size());
                for (String key : getButtons().keySet()) {
                    if (line.contains("[" + key + "]")) {
                        //TODO implement perm check
                        getButtons().get(key).execute(user, event.getClickedBlock());
                    }
                }
            }
        }
    }
}
