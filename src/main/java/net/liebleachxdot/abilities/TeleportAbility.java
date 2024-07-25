package net.liebleachxdot.abilities;

import net.liebleachxdot.abilities.cooldowns.Cooldown;
import net.liebleachxdot.abilities.effects.TeleportEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportAbility implements Ability{

    private final Player player;

    private final Player attacker;

    private final Cooldown cooldownsMap;



    public TeleportAbility(Player player, Player attacker) {
        this.player = player;
        this.attacker = attacker;
        this.cooldownsMap = new Cooldown();
    }

    @Override
    public void ability() {
        if (!cooldownsMap.tryUseAbility(attacker, "teleport", 5)) {
            attacker.sendMessage("Способность находится на перезарядке!");
            return;
        }

        Location targetLocation = player.getLocation();
        Location behindTarget = targetLocation.clone().add(targetLocation.getDirection().multiply(-1));
        behindTarget.setY(targetLocation.getY());

        attacker.teleport(behindTarget);
        new TeleportEffect(player).effect();
    }
}
