package dev.devous.vandium.menu;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ShapedMenuPattern {

    private final LinkedList<Character> icons = new LinkedList<>();
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, Button> buttons = new HashMap<>();

    public ShapedMenuPattern(char[]... rows) {
        setPattern(rows);
    }

    public void set(char c, ItemStack item) {
        int index = 0;
        for (char icon : icons) { if (icon == c) { items.put(index, item); } index++; }
    }

    public void set(char c, Button button) {
        int index = 0;
        for (char icon : icons) { if (icon == c) { buttons.put(index, button); } index++; }
    }

    public void setPattern(char[][] rows) {
        for (char[] row : rows) for (char c : row) icons.add(c);
    }

    public void clear() { icons.clear(); items.clear(); buttons.clear(); }

    public Map<Integer, ItemStack> getItems() { return items; }

    public Map<Integer, Button> getButtons() { return buttons; }

}
