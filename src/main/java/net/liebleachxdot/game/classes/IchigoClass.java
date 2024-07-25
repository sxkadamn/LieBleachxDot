package net.liebleachxdot.game.classes;

import net.liebleachxdot.abilities.TeleportAbility;
import net.liebleachxdot.tools.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IchigoClass implements Listener, BClasses {

    private final List<Player> players;

    private final String name;


    private final int maxPlayers;

    public IchigoClass() {
        this.players = new ArrayList<>();
        this.name = "&4&lИчиго";
        this.maxPlayers = 1;
    }

    @Override
    public void grantAbilities() {
        for(Player player : players) {
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD));
        }
    }

    @Override
    public void addPlayer(Player player) {

        if(players.contains(player))
            return;

        players.add(player);

        Utility.sendMessage(player, "&6Теперь ты владеешь классом " + name, false);
    }

    @EventHandler
    public void listenerAbility(EntityDamageByEntityEvent event) {

        if(!(event.getDamager() instanceof Player)) return;

        if(!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (new Random().nextDouble() <= 0.3) {
            if (players.contains(attacker)) {
                new TeleportAbility(target, attacker).ability();
            }
        }
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
