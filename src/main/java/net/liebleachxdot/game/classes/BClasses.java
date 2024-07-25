package net.liebleachxdot.game.classes;

import org.bukkit.entity.Player;

import java.util.List;

public interface BClasses {


    void grantAbilities();

    void addPlayer(Player player);

    List<Player> getPlayers();
    String getName();

}
