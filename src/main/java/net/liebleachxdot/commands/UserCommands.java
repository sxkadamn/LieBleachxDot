package net.liebleachxdot.commands;

import net.liebleachxdot.api.BaseCommand;
import net.liebleachxdot.game.Arena;
import net.liebleachxdot.gui.create.ArenasGui;
import net.liebleachxdot.tools.Utility;;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommands extends BaseCommand {

    public UserCommands(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Utility.sendMessage(sender, "Only players can execute this command!", true);
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            Utility.sendMessage(player, "Please provide a valid command!", true);
            return;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "join":
                if (args.length == 1) {
                    Utility.sendMessage(player, "Specify the arena name", true);
                    return;
                }

                Arena joinArena = Arena.get(args[1]);
                if (joinArena == null) {
                    Utility.sendMessage(player, "Arena does not exist.", true);
                    return;
                }

                if (!joinArena.join(player)) {
                    Utility.sendMessage(player, "Unable to join.", true);
                }
                break;

            case "leave":
                Arena leaveArena = Arena.get(player);
                if (leaveArena == null) {
                    Utility.sendMessage(player, "You are not in an arena.", true);
                    return;
                }

                leaveArena.leave(player);
                Utility.sendMessage(player, "You have left the arena.", true);
                break;

            case "gui":
                ArenasGui arenasGui = new ArenasGui();
                arenasGui.arenaGui(player);
                break;

            default:
                Utility.sendMessage(player, "Unknown command. Please use a valid command!", true);
                break;
        }
    }
}