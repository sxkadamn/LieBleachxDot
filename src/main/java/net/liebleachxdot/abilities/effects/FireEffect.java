package net.liebleachxdot.abilities.effects;

import net.liebleachxdot.LieBleachxDot;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FireEffect implements Effect{
    private final Player player;

    public FireEffect(Player player) {
        this.player = player;
    }

    @Override
    public void effect() {

        new BukkitRunnable() {
            double angle = 0;
            final double radius = 3.0;
            final int duration = 2;
            int count = 0;

            @Override
            public void run() {
                if (count >= duration) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < 360; i += 10) {
                    double radians = Math.toRadians(i + angle);
                    double x = Math.cos(radians) * radius;
                    double z = Math.sin(radians) * radius;

                    Location loc = player.getLocation().clone().add(x, 1, z);
                    player.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0, 0);
                }

                player.damage(1);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                angle += 10;
                count++;
            }
        }.runTaskTimer(LieBleachxDot.getInstance(), 0, 2);
    }
}
