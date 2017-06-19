package me.minidigger.voxelgameslib.signs;

import net.kyori.text.TextComponent;

import java.util.HashMap;
import java.util.Map;

public class SignButtons {

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
}
