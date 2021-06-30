package dev.devous.vandium.message.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static dev.devous.vandium.color.Color.color;

public class TextComponentBuilder {

    private final TextComponent textComponent;


    /**
     * Constructor for new class instance.
     *
     * @param text the message in chat.
     */
    public TextComponentBuilder(String text) {
        this.textComponent = new TextComponent(color(text));
    }

    /**
     * Add a hover event to the text component.
     * <p/>
     * When you move your mouse over the message in chat it will display information.
     *
     * @param action  what happens when you hover. types: SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM, SHOW_ENTITY
     * @param value   the text shown upon hover.
     * @return class  instance.
     */
    public TextComponentBuilder hover(HoverEvent.Action action, String... value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            sb.append(color(value[i]));
            if (i != value.length - 1) {
                sb.append("\n");
            }
        }
        textComponent.setHoverEvent(new HoverEvent(action, new ComponentBuilder(sb.toString()).create()));
        return this;
    }


    /**
     * Add a click event to the text component.
     * <p/>
     * When you click on the message in chat it will complete the specified action.
     *
     * @param action  what happens when you click. types: OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE
     * @param value   the value to go with the action.
     * @return class instance.
     */
    public TextComponentBuilder click(ClickEvent.Action action, String value) {
        textComponent.setClickEvent(new ClickEvent(action, value));
        return this;
    }

    /**
     * Get the end text component.
     *
     * @return final text component.
     */
    public TextComponent build() {
        return textComponent;
    }

    /**
     * Send the text component to the player (rather than getting an object)
     *
     * @param player player to send the component to.
     */
    public void send(Player player) {
        player.spigot().sendMessage(textComponent);
    }

}
