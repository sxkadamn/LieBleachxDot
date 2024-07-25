package net.liebleachxdot.game.listeners;

import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.game.Arena;
import net.liebleachxdot.game.Game;
import net.liebleachxdot.team.TeamModel;
import net.liebleachxdot.team.TeamPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameListener implements Listener {


    @EventHandler
    public void onUserDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player target = (Player) event.getEntity();
        Arena arena = Arena.get(target);

        if (arena == null || !arena.getPlayers().contains(target)) {
            return;
        }

        double finalDamage = target.getHealth() - event.getFinalDamage();

        TeamPlayer targetPlayer = arena.getGame().getTeamPlayer(target);
        if (finalDamage <= 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                        if(targetPlayer.getTeamModel().getPickName().equalsIgnoreCase("gotei")) {
                            targetPlayer.getPlayer().teleport(arena.getPos1());
                        }
                        else if(targetPlayer.getTeamModel().getPickName().equalsIgnoreCase("espada")) {
                            targetPlayer.getPlayer().teleport(arena.getPos2());
                        }
                    targetPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    targetPlayer.matchPlayer();
                    cancel();
                }
            }.runTaskLater(LieBleachxDot.getInstance(), 100L);

            if(targetPlayer.getTeamModel().getPickName().equalsIgnoreCase("gotei")) {

                arena.setGoteiScores(arena.getGoteiScores() + 1);
            }
            else if(targetPlayer.getTeamModel().getPickName().equalsIgnoreCase("espada")) {

                arena.setEspadaScores(arena.getEspadaScores() + 1);
            }
            event.setCancelled(true);
            target.setGameMode(GameMode.SPECTATOR);
            target.setHealth(target.getMaxHealth());
            target.teleport(arena.getLobby());
        }
    }
}
