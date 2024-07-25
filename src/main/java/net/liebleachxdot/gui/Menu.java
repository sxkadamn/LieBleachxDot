package net.liebleachxdot.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private final String id;
    private final String title;
    private Inventory inventory;
    private final HashMap<Integer, MenuSlot> slots;
    private boolean interactDisabled = true;
    private CloseListener closeListener;

    public Menu(String id, String title, int rows) {
        this.id = id;
        inventory = Bukkit.createInventory(null, rows * 9, title);
        this.title = title;
        slots = new HashMap<>();
    }

    public Menu(String id, InventoryHolder holder, String title, InventoryType type) {
        this.id = id;
        this.title = title;
        inventory = Bukkit.createInventory(holder, type, title);
        slots = new HashMap<>();
    }

    public String getID() {
        return id;
    }

    public String getTitle() { return title; }

    public Inventory getInventory() {
        return inventory;
    }

    @Deprecated
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        slots.clear();
    }

    public MenuSlot getSlot(int position) {
        return slots.get(position);
    }

    public Menu setSlot(int position, MenuSlot slot) {
        slot.setPosition(position);
        slots.put(position, slot);
        inventory.setItem(position, slot.getItem());
        return this;
    }

    public Menu disableInteract(boolean disable) {
        interactDisabled = disable;
        return this;
    }

    public boolean isInteractDisabled() {
        return interactDisabled;
    }

    public Menu removeSlot(int position) {
        slots.remove(position);
        inventory.clear(position);
        return this;
    }

    public void refreshItems() {
        inventory.clear();
        for (Map.Entry<Integer, MenuSlot> entry : slots.entrySet())
            inventory.setItem(entry.getKey(), entry.getValue().getItem());
        for (HumanEntity player : inventory.getViewers())
            ((Player) player).updateInventory();
    }

    public Menu refreshSlot(int slot) {
        if (!slots.containsKey(slot)) inventory.clear(slot);
        else inventory.setItem(slot, slots.get(slot).getItem());
        for (HumanEntity player : inventory.getViewers())
            ((Player) player).updateInventory();
        return this;
    }

    public int getSlotPosition(MenuSlot slot) {
        for (Map.Entry<Integer, MenuSlot> entry : slots.entrySet())
            if (entry.getValue().equals(slot)) return entry.getKey();
        return -1;
    }

    public boolean hasSlot(int slot) {
        return slots.containsKey(slot);
    }

    public boolean hasCloseListener() {
        return closeListener != null;
    }

    public CloseListener getCloseListener() {
        return closeListener;
    }

    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

}




