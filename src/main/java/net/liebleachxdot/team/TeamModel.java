package net.liebleachxdot.team;


import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamModel {

    private final String teamName;
    private final String pickName;
    private final List<Player> players;

    public TeamModel(String teamName, String pickName) {
        this.teamName = teamName;
        this.pickName = pickName;
        this.players = new ArrayList<>();
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPickName() {
        return pickName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }
}
