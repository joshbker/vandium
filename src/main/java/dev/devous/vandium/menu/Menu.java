package dev.devous.vandium.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dev.devous.vandium.color.Color.color;

public abstract class Menu implements Listener {

    private final JavaPlugin core;

    private Inventory inventory;
    private final int rows;
    private final String title;

    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, Button> buttons = new HashMap<>();

    public Menu(JavaPlugin core, String title, int rows) {
        this.core = core;
        this.title = color(title);
        this.rows = rows;

        inventory = Bukkit.createInventory(null, rows * 9, title);
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    public void applyMenuPattern(ShapedMenuPattern pattern) {
        items.putAll(pattern.getItems());
        buttons.putAll(pattern.getButtons());
    }

    public int firstEmpty() {
        for (int i = 0; i < rows * 9; i++) {
            if (items.containsKey(i) || buttons.containsKey(i)) continue;
            return i;
        }
        return rows * 9;
    }

    public void setInventoryType(InventoryType type) {
        inventory = Bukkit.createInventory(null, type, title);
    }

    public void setItem(int slot, ItemStack item) { items.put(slot, item); }

    public ItemStack getItem(int slot) { return inventory.getItem(slot); };

    public void setButton(int slot, Button button) { buttons.put(slot, button); }

    public Menu build() {
        if (!items.isEmpty()) for (int i : items.keySet()) inventory.setItem(i, items.get(i));
        if (!buttons.isEmpty()) for (int i : buttons.keySet()) inventory.setItem(i, buttons.get(i).getIcon());
        return this;
    }

    public void open(Player player) { player.openInventory(inventory); }

    public void update(Player player) { player.closeInventory(); open(player); }

    public void updateAll() { for (HumanEntity e : inventory.getViewers()) { e.closeInventory(); open((Player) e); } }

    public void close() {
        InventoryCloseEvent.getHandlerList().unregister(this);
        items.clear(); buttons.clear();
        Bukkit.getScheduler().runTaskLater(core, () -> { for (HumanEntity e : new ArrayList<>(inventory.getViewers())) e.closeInventory(); }, 1);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        if (!inventory.getViewers().contains(e.getWhoClicked()) || !(e.getWhoClicked() instanceof Player)) return;
        e.setCancelled(true);
        if (buttons.containsKey(e.getSlot())) buttons.get(e.getSlot()).clicked((Player) e.getWhoClicked());
    }

}
