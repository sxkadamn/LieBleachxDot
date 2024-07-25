package net.liebleachxdot.gui.impl;


import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.gui.Menu;
import net.liebleachxdot.gui.MenuSlot;
import net.liebleachxdot.gui.SlotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.NOTHING || event.isCancelled()) return;
        Inventory clicked = event.getInventory();
        Menu menu = LieBleachxDot.getManager().getMenu(clicked);
        if (menu != null) {
            if (menu.isInteractDisabled()) event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (player != null) {
                MenuSlot slot = menu.getSlot(event.getSlot());
                if (slot != null) {
                    if (!event.isCancelled() && slot.isInteractDisabled() && event.getClickedInventory().equals(event.getView().getTopInventory()))
                        event.setCancelled(true);
                    if (slot.hasListener()) {
                        SlotListener listener = slot.getListener();
                        if (event.getClick() == ClickType.LEFT)
                            listener.onLeftClick(player);
                        else if (event.getClick() == ClickType.MIDDLE)
                            listener.onMiddleClick(player);
                        else if (event.getClick() == ClickType.RIGHT)
                            listener.onRightClick(player);
                        else if (event.getClick() == ClickType.SHIFT_LEFT)
                            listener.onShiftLeft(player);
                        else if (event.getClick() == ClickType.SHIFT_RIGHT)
                            listener.onShiftRight(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        Inventory clicked = event.getView().getTopInventory();
        Menu menu =  LieBleachxDot.getManager().getMenu(clicked);
        if (menu != null) {
            if (menu.isInteractDisabled()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory closed = event.getInventory();
        Menu menu =  LieBleachxDot.getManager().getMenu(closed);
        if (menu != null) {
            if (menu.hasCloseListener())
                menu.getCloseListener().run(((Player)event.getPlayer()));
        }
    }

}