package dev.devous.vandium.color;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Color {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] color(String... messages) {
        return colorList(messages).toArray(new String[0]);
    }

    public static List<String> colorList(List<String> messages) {
        List<String> colored = new ArrayList<>();
        for (String message : messages) {
            colored.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        return colored;
    }

    public static List<String> colorList(String... messages) {
        List<String> colored = new ArrayList<>();
        for (String message : messages) {
            colored.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        return colored;
    }

}
