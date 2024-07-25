package net.liebleachxdot.commands;

import net.liebleachxdot.game.Arena;
import net.liebleachxdot.tools.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommands implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)){
            Utility.sendMessage(sender, "Только игрок может выполнять эту команду!", true);
            return true;
        }

        Player player = (Player) sender;

        if (player.isOp()) {
            if (args[0].equalsIgnoreCase("list")){
                for (Arena arena: Arena.getArenas()){
                    Utility.sendMessage(player, "- " + arena.getName(), false);
                }
            }
            else if (args[0].equalsIgnoreCase("joinAll")){
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena == null) {
                    Utility.sendMessage(player, "Такой арены не существует!", true);
                    return true;
                }

                for (Player onlinePlayer: Bukkit.getOnlinePlayers()){
                    arena.join(onlinePlayer);
                }

                Utility.sendMessage(player, "Выполнено!", false);
            }
            else if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena != null) {
                    Utility.sendMessage(player, "Арена с таким именем уже существует!", true);

                    return true;
                }

                Arena.add(args[1]);

                Utility.sendMessage(player, "Арена создана!", false);
                return true;
            } else if (args[0].equalsIgnoreCase("setPos1")) {
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena == null) {
                    Utility.sendMessage(player, "Такой арены не существует", true);

                    return true;
                }

                arena.setPos1(player.getLocation());
                Utility.sendMessage(player, "Первая позиция установлена", false);
                return true;
            }else if (args[0].equalsIgnoreCase("remove")){
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena == null) {
                    Utility.sendMessage(player, "Такой арены не существует", true);
                    return true;
                }
            }
            else if (args[0].equalsIgnoreCase("setPos2")) {
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena == null) {
                    Utility.sendMessage(player, "Такой арены не существует", true);
                    return true;
                }

                arena.setPos2(player.getLocation());
                Utility.sendMessage(player, "Вторая позиция установлена", false);

                return true;
            } else if (args[0].equalsIgnoreCase("setLobby")) {
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena == null) {
                    Utility.sendMessage(player, "Такой арены не существует", true);
                    return true;
                }

                arena.setLobby(player.getLocation());
                Utility.sendMessage(player, "Лобби установлено", false);

                return true;
            } else if (args[0].equalsIgnoreCase("launch")) {
                if (args.length == 1) {
                    Utility.sendMessage(player, "Укажи имя арены", true);
                    return true;
                }

                Arena arena = Arena.get(args[1]);
                if (arena == null) {
                    Utility.sendMessage(player, "Такой арены не существует", true);
                    return true;
                }

                if (arena.launch())
                    Utility.sendMessage(player, "Арена запущена", false);

                else
                    Utility.sendMessage(player, "Невозможно запустить. Возможно, ты забыл указать лобби, " +
                            "точку спавна атакующих или карта слишком маленькая", true);

                return true;
            }
        }
        else {
            Utility.sendMessage(player, "Недостаточно прав!", true);
        }

        return false;
    }
}
