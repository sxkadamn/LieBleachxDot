package net.liebleachxdot.gui.impl;

import net.liebleachxdot.gui.Menu;
import net.liebleachxdot.gui.Text;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;


import java.util.Collection;
import java.util.HashMap;

public final class MenuManager {

    private final HashMap<String, HashMap<String, Menu>> guis;
    private final HashMap<Inventory, Menu> inv_guis;

    public MenuManager() {
        guis = new HashMap<>();
        inv_guis = new HashMap<>();
    }

    public Menu createGUI(String id, String player, String title, int rows) {
        Menu menu = new Menu(id, title, rows);
        if (!guis.containsKey(player)) guis.put(player, new HashMap<>());
        if (id != null && !id.isEmpty()) {
            guis.get(player).put(id, menu);
            inv_guis.put(menu.getInventory(), menu);
        }
        return menu;
    }

    public Menu createGUI(String id, String player, Text title, int rows) {
        Menu menu = new Menu(id, title.getRaw(), rows);
        if (!guis.containsKey(player)) guis.put(player, new HashMap<>());
        if (id != null && !id.isEmpty()) {
            guis.get(player).put(id, menu);
            inv_guis.put(menu.getInventory(), menu);
        }
        return menu;
    }

    public Menu createGUI(String id, String player, String title, InventoryType inventoryType) {
        Menu menu = new Menu(id, Bukkit.getPlayerExact(player), title, inventoryType);
        if (!guis.containsKey(player)) guis.put(player, new HashMap<>());
        if (id != null && !id.isEmpty()) {
            guis.get(player).put(id, menu);
            inv_guis.put(menu.getInventory(), menu);
        }
        return menu;
    }

    public void registerGUI(String id, String player, Menu menu) {
        if (!guis.containsKey(player)) guis.put(player, new HashMap<>());
        guis.get(player).put(id, menu);
        inv_guis.put(menu.getInventory(), menu);
    }

    public Collection<Menu> getMenus(String player) {
        if (!guis.containsKey(player)) guis.put(player, new HashMap<>());
        return guis.get(player).values();
    }

    public Menu getMenu(Inventory inventory) {
        return inv_guis.get(inventory);
    }

    public Menu getMenu(String player, String id) {
        if (!guis.containsKey(player)) guis.put(player, new HashMap<>());
        return guis.get(player).get(id);
    }

    public void removeMenus(String name) {
        guis.remove(name);
    }

}