package net.liebleachxdot.commands;

import net.liebleachxdot.game.Arena;
import net.liebleachxdot.gui.create.ArenasGui;
import net.liebleachxdot.tools.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserCommands implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)){
            Utility.sendMessage(sender, "Только игрок может выполнять эту команду!", true);
            return true;
        }

        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("join")){
            if (args.length == 1) {
                Utility.sendMessage(player, "Укажи имя арены", true);
                return true;
            }

            Arena arena = Arena.get(args[1]);
            if (arena == null) {
                Utility.sendMessage(player, "Такой арены не существует.", true);
                return true;
            }

            if (!arena.join(player)) Utility.sendMessage(player, "Невозможно присоединиться.", true);

            return true;
        }
        else if (args[0].equalsIgnoreCase("leave")){
            Arena arena = Arena.get(player);
            if (arena == null){
                Utility.sendMessage(player, "Ты не на арене.", true);
                return true;
            }

            arena.leave(player);
            Utility.sendMessage(player, "Вы покинули арену.", true);
            return true;
        }
        else if (args[0].equalsIgnoreCase("gui")) {
            ArenasGui arenasGui = new ArenasGui();
            arenasGui.arenaGui(player);
        }

        return false;
    }
}