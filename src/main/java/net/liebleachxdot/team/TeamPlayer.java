package net.liebleachxdot.team;

import net.liebleachxdot.tools.Utility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamPlayer {

    private TeamModel teamModel;
    private Player player;

    public TeamPlayer(TeamModel teamModel, Player player) {
        this.teamModel = teamModel;
        this.player = player;

        player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                "&6Твоя команда " + teamModel.getTeamName()),
                "",
                10,
                10,
                10);
        matchPlayer();
    }


    public void matchPlayer() {
        ClassSelection selection = ClassSelection.fromName(teamModel.getTeamName());
        if (selection != null) {
            ItemStack itemStack = Utility.createTeamSelector(selection);
            player.getInventory().addItem(itemStack);
        }
    }


    public TeamModel getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
