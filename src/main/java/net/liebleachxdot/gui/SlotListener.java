package net.liebleachxdot.gui;

import org.bukkit.entity.Player;


public class SlotListener {

    private MenuSlot slot;

    public boolean onLeftClick(Player player) {
        return false;
    }

    public boolean onMiddleClick(Player player) {
        return onLeftClick(player);
    }

    public boolean onRightClick(Player player) {
        return onLeftClick(player);
    }

    public boolean onShiftLeft(Player player) {
        return onLeftClick(player);
    }

    public boolean onShiftRight(Player player) {
        return onLeftClick(player);
    }

    public MenuSlot getSlot() {
        return slot;
    }

    protected void setSlot(MenuSlot slot) {
        this.slot = slot;
    }
}

