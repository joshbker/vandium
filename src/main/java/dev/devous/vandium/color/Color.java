package dev.devous.vandium.color;

import org.bukkit.ChatColor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Color {

    private Color() {

    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] color(String... messages) {
        return colorList(messages).toArray(new String[0]);
    }

    public static List<String> colorList(List<String> messages) {
        return colorizeList(messages);
    }

    public static List<String> colorList(String... messages) {
        return colorizeList(Arrays.asList(messages));
    }

    private static List<String> colorizeList(Collection<String> messages) {
        return messages.stream()
                .map(Color::color)
                .collect(Collectors.toList());
    }

}
