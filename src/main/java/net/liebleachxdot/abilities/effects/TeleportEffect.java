package net.liebleachxdot.abilities.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class TeleportEffect implements Effect{

    private final Player player;

    public TeleportEffect(Player player) {
        this.player = player;
    }

    @Override
    public void effect() {
        Location location = player.getLocation();
        location.getWorld().spawnParticle(Particle.PORTAL, location, 200, 1.0, 1.0, 1.0, 0.2);
        location.getWorld().spawnParticle(Particle.END_ROD, location, 100, 0.5, 0.5, 0.5, 0.1);

        double radius = 1.0;
        for (int i = 0; i < 360; i += 20) {
            double angle = Math.toRadians(i);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location particleLocation = location.clone().add(x, 0, z);
            location.getWorld().spawnParticle(Particle.FLAME, particleLocation, 10, 0.1, 0.1, 0.1, 0.01);
        }

        location.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }
}
