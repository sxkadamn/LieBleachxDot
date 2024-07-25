package net.liebleachxdot.abilities;

import net.liebleachxdot.abilities.cooldowns.Cooldown;
import net.liebleachxdot.abilities.effects.FireEffect;
import org.bukkit.entity.Player;

public class FireAbility implements Ability{

    private final Player player;

    private final Player attacker;

    private final Cooldown cooldownsMap;

    public FireAbility(Player player, Player attacker) {
        this.player = player;
        this.attacker = attacker;
        this.cooldownsMap = new Cooldown();
    }

    @Override
    public void ability() {
        if (!cooldownsMap.tryUseAbility(attacker, "fire", 5)) {
            attacker.sendMessage("Способность находится на перезарядке!");
            return;
        }

        player.setFireTicks(100);
        new FireEffect(player).effect();

    }
}
