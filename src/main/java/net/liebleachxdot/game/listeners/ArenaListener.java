package net.liebleachxdot.game.listeners;

import net.liebleachxdot.game.Arena;
import net.liebleachxdot.gui.create.ArenasGui;
import net.liebleachxdot.team.ClassSelection;
import net.liebleachxdot.team.TeamPlayer;
import net.liebleachxdot.tools.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ArenaListener implements Listener {

    //@EventHandler
    //public void onLeaveZone(PlayerMoveEvent event) {
    //    Player player = event.getPlayer();
    //    Arena arena = Arena.get(player);
    //    if (arena != null) {
    //        if (!arena.isOpen()) {
    //            if (event.getTo().getX() > arena.getPos2().getX() ||
    //                    event.getTo().getZ() > arena.getPos2().getZ() ||
    //                    event.getTo().getX() < arena.getPos1().getX() ||
            //                    event.getTo().getZ() < arena.getPos1().getZ()) {
    //                event.setCancelled(true);
    //                player.sendMessage(ChatColor.GOLD + "[" + arena.getName() + "]: " + ChatColor.AQUA + "Ты пытаешься покинуть зону игры!");
    //           }
    //         }
    //    }
    // }//

    @EventHandler
    public void onClickItem(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        Arena arena = Arena.get(player);

        if (arena == null || itemStack == null || itemStack.getItemMeta() == null) {
            return;
        } else {
            itemStack.getItemMeta().getDisplayName();
        }

        String displayName = Utility.rgb(itemStack.getItemMeta().getDisplayName());

        for (ClassSelection selection : ClassSelection.values()) {
            if (displayName.equalsIgnoreCase(Utility.rgb(selection.getDisplayName()))) {
                ArenasGui arenasGui = new ArenasGui();
                switch (selection) {
                    case GOTEI:
                        arenasGui.chooseClassesGuiGotei(player, arena);
                        break;
                    case ESPADA:
                        arenasGui.chooseClassesGuiEspada(player, arena);
                        break;
                }
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onJoinServer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack arenaSelectionItem = Utility.createItem(Material.DIAMOND, "&6Выбор арены <--- CLICK");
        player.getInventory().addItem(arenaSelectionItem);
    }

    @EventHandler
    public void onServerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Arena arena = Arena.get(player);

        if (arena != null) {
            arena.leave(player);

            if(arena.isClassesChoose()) {
                arena.setClassesChoose(false);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Arena arena = Arena.get(player);

        if (arena != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttackTeam(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        if (!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        Arena arena = Arena.get(attacker);

        if (arena != null) {
            TeamPlayer targetTeam = arena.getGame().getTeamPlayer(target);
            TeamPlayer attackerTeam = arena.getGame().getTeamPlayer(attacker);

            if (targetTeam != null && attackerTeam != null) {
                if (targetTeam.getTeamModel().equals(attackerTeam.getTeamModel())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
