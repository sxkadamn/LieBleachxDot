package net.liebleachxdot.abilities.cooldowns;

import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.abilities.effects.TeleportEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown{

    private static final Map<UUID, Map<String, Long>> playerCooldowns = new HashMap<>();

    public void startActionBar(Player player, String abilityName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!hasCooldown(player, abilityName)) {
                    playerCooldowns.get(player.getUniqueId()).remove(abilityName);
                    this.cancel();
                    return;
                }
                int timeLeft = getCooldown(player, abilityName);
                player.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&6Перезарядка способности: " + timeLeft + " секунд"));
            }
        }.runTaskTimer(LieBleachxDot.getInstance(), 0L, 20L);
    }

    public boolean tryUseAbility(Player attacker, String abilityName,  int seconds) {
        if (hasCooldown(attacker, abilityName)) {
            return false;
        }

        setCooldown(attacker, abilityName, seconds);

        startActionBar(attacker, abilityName);

        return true;
    }

    private void setCooldown(Player player, String abilityName, int seconds) {
        UUID playerId = player.getUniqueId();
        if (!playerCooldowns.containsKey(playerId)) {
            playerCooldowns.put(playerId, new HashMap<>());
        }
        playerCooldowns.get(playerId).put(abilityName, System.currentTimeMillis() + (seconds * 1000L));
    }

    private boolean hasCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();
        return playerCooldowns.containsKey(playerId)
                && playerCooldowns.get(playerId).containsKey(abilityName)
                && playerCooldowns.get(playerId).get(abilityName) > System.currentTimeMillis();
    }

    private int getCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();
        if (!playerCooldowns.containsKey(playerId) || !playerCooldowns.get(playerId).containsKey(abilityName)) {
            return 0;
        }
        long timeLeft = (playerCooldowns.get(playerId).get(abilityName) - System.currentTimeMillis()) / 1000L;
        return (int) timeLeft;
    }

}
