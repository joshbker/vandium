package dev.devous.vandium.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    private ItemStack icon;

    public abstract void clicked(Player player);

    public ItemStack getIcon() {
        return icon;
    }

    public Button setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

}
