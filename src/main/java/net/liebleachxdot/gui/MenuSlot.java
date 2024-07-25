package net.liebleachxdot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuSlot {

    private ItemStack item;
    private SlotListener listener;
    private boolean interactDisabled;
    private int position;
    private boolean staticated;

    public MenuSlot(ItemStack item) {
        this.item = item;
    }

    public MenuSlot(Material material) {
        item = new ItemStack(material);
    }

    public MenuSlot(Material material, Text display, List<Text> lore) {
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display.getRaw());
        if (lore != null) {
            List<String> list = new ArrayList<>();
            for (Text text : lore)
                list.add(text.getRaw());
            meta.setLore(list);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    public MenuSlot setPlayerOwner(String name) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public void setStatic(final boolean staticated) {
        this.staticated = staticated;
    }

    public boolean isStatic() {
        return staticated;
    }

    public ItemStack getItem() {
        return item;
    }

    public MenuSlot setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public MenuSlot setAmount(int i) {
        this.item.setAmount(i);
        return this;
    }

    public MenuSlot setDisplay(String siplay) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(siplay);
        item.setItemMeta(meta);
        return this;
    }

    public MenuSlot setLoreList(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>(lore);
        meta.setLore(list);
        item.setItemMeta(meta);
        return this;
    }


    public MenuSlot setLore(Text... lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        for (Text text : lore)
            list.add(text.getRaw());
        meta.setLore(list);
        item.setItemMeta(meta);
        return this;
    }

    public MenuSlot setLore(String... lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        for (String text : lore)
            list.add(text);
        meta.setLore(list);
        item.setItemMeta(meta);
        return this;
    }

    public MenuSlot setListener(SlotListener listener) {
        listener.setSlot(this);
        this.listener = listener;
        return this;
    }

    public SlotListener getListener() {
        return listener;
    }

    public boolean hasListener() {
        return listener != null;
    }

    public MenuSlot hideAttributes(boolean removeAttributes) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (removeAttributes) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
            else {
                meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.removeItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.removeItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    public MenuSlot setEnchanted(boolean enchanted) {
        if (enchanted) item.addUnsafeEnchantment(Enchantment.LURE, 1);
        else item.removeEnchantment(Enchantment.LURE);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return this;
    }

    public MenuSlot disableInteract(boolean disable) {
        interactDisabled = disable;
        return this;
    }

    public boolean isInteractDisabled() {
        return interactDisabled;
    }

    public int getPosition() {
        return position;
    }


    protected void setPosition(int position) {
        this.position = position;
    }
}