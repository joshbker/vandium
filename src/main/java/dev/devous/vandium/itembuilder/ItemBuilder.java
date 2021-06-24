package dev.devous.vandium.itembuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static dev.devous.vandium.color.Color.color;
import static dev.devous.vandium.color.Color.colorList;

public class ItemBuilder implements Cloneable {

    private final ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int data) {
        item = new ItemStack(material, 1, (short) data);
        meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        meta = item.getItemMeta();
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(color(name));
        return this;
    }

    public ItemBuilder lore(String... lore) {
        meta.setLore(colorList(lore));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        meta.setLore(colorList(lore));
        return this;
    }

    public ItemBuilder lore(String lore) {
        return this.lore(Collections.singletonList(lore));
    }

    public ItemBuilder addLore(String... lore) {
        List<String> cLore = meta.getLore();
        if (cLore == null) cLore = new ArrayList<>();
        cLore.addAll(Arrays.asList(lore));
        return this.lore(cLore);
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> cLore = meta.getLore();
        if (cLore == null) cLore = new ArrayList<>();
        cLore.addAll(lore);
        return this.lore(cLore);
    }

    public ItemBuilder addLore(String lore) {
        List<String> cLore = meta.getLore();
        if (cLore == null) cLore = new ArrayList<>();
        cLore.add(lore);
        return this.lore(cLore);
    }

    public ItemBuilder durability(int d) {
        item.setDurability((short) d);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchant, int level) {
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchant, level);
        meta = item.getItemMeta();
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchant) {
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchant, 1);
        meta = item.getItemMeta();
        return this;
    }

    public ItemBuilder type(Material mat) { item.setType(mat); return this; }

    public ItemBuilder clearLore() { meta.setLore(new ArrayList<>()); return this; }

    public ItemBuilder clearEnchantments() {
        for (Enchantment e : item.getEnchantments().keySet()) item.removeEnchantment(e); return this;
    }

    public ItemBuilder woolColor(DyeColor color) { return this.durability(color.getWoolData()); }

    public ItemBuilder dyeColor(DyeColor color) { return this.durability(color.getDyeData()); }

    public ItemBuilder glow() {
        this.enchantment(Enchantment.WATER_WORKER);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder texture(String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemBuilder setSkull(String owner) {
        String sUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + Bukkit.getOfflinePlayer(owner).getUniqueId().toString();
        try {
            URLConnection req = new URL(sUrl).openConnection(); req.connect();
            JsonObject rootObj = new JsonParser().parse(new InputStreamReader((InputStream) req.getContent())).getAsJsonObject();
            JsonArray array = rootObj.get("properties").getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.get("name") != null && obj.get("name").getAsString().equals("textures")) {
                    return this.texture(obj.get("value").getAsString());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta); return this.item;
    }

    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}