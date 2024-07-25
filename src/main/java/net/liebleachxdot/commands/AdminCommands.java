package net.liebleachxdot.commands;

import net.liebleachxdot.api.BaseCommand;
import net.liebleachxdot.game.Arena;
import net.liebleachxdot.tools.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands extends BaseCommand {


    public AdminCommands(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Utility.sendMessage(sender, "Only players can execute this command!", true);
            return;
        }

        Player player = (Player) sender;

        if (player.isOp()) {
            if (args.length == 0) {
                Utility.sendMessage(player, "Please provide a valid command!", true);
                return;
            }

            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "list":
                    for (Arena arena : Arena.getArenas()) {
                        Utility.sendMessage(player, "- " + arena.getName(), false);
                    }
                    break;

                case "joinall":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena joinArena = Arena.get(args[1]);
                    if (joinArena == null) {
                        Utility.sendMessage(player, "Arena does not exist!", true);
                        return;
                    }

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        joinArena.join(onlinePlayer);
                    }

                    Utility.sendMessage(player, "All players joined the arena!", false);
                    break;

                case "create":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena createArena = Arena.get(args[1]);
                    if (createArena != null) {
                        Utility.sendMessage(player, "Arena already exists!", true);
                        return;
                    }

                    Arena.add(args[1]);
                    Utility.sendMessage(player, "Arena created!", false);
                    break;

                case "setpos1":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena setPos1Arena = Arena.get(args[1]);
                    if (setPos1Arena == null) {
                        Utility.sendMessage(player, "Arena does not exist!", true);
                        return;
                    }

                    setPos1Arena.setPos1(player.getLocation());
                    Utility.sendMessage(player, "First position set!", false);
                    break;

                case "setpos2":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena setPos2Arena = Arena.get(args[1]);
                    if (setPos2Arena == null) {
                        Utility.sendMessage(player, "Arena does not exist!", true);
                        return;
                    }

                    setPos2Arena.setPos2(player.getLocation());
                    Utility.sendMessage(player, "Second position set!", false);
                    break;

                case "setlobby":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena setLobbyArena = Arena.get(args[1]);
                    if (setLobbyArena == null) {
                        Utility.sendMessage(player, "Arena does not exist!", true);
                        return;
                    }

                    setLobbyArena.setLobby(player.getLocation());
                    Utility.sendMessage(player, "Lobby set!", false);
                    break;

                case "launch":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena launchArena = Arena.get(args[1]);
                    if (launchArena == null) {
                        Utility.sendMessage(player, "Arena does not exist!", true);
                        return;
                    }

                    if (launchArena.launch()) {
                        Utility.sendMessage(player, "Arena launched!", false);
                    } else {
                        Utility.sendMessage(player, "Cannot launch arena. Ensure you have set the lobby, attacker spawn point, and that the map size is adequate.", true);
                    }
                    break;

                case "remove":
                    if (args.length == 1) {
                        Utility.sendMessage(player, "Specify the arena name", true);
                        return;
                    }

                    Arena removeArena = Arena.get(args[1]);
                    if (removeArena == null) {
                        Utility.sendMessage(player, "Arena does not exist!", true);
                        return;
                    }

                    break;

                default:
                    Utility.sendMessage(player, "Unknown command. Please use a valid command!", true);
                    break;
            }
        } else {
            Utility.sendMessage(player, "You do not have permission to execute this command!", true);
        }
    }

}
