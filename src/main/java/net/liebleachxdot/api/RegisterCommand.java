package net.liebleachxdot.api;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand extends Command implements PluginIdentifiableCommand {
    private final Plugin plugin;

    private final CommandExecutor owner;

    private static CommandMap commandMap;


    public RegisterCommand(List<String> aliases, String desc, String usage, CommandExecutor owner, Plugin plugin) {
        super(aliases.get(0), desc, usage, aliases);
        this.owner = owner;
        this.plugin = plugin;
    }

    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        return this.owner.onCommand(sender, this, label, args);
    }

    public static void reg(Plugin plugin, CommandExecutor executor, List<String> aliases, String desc, String usage) {
        try {
            RegisterCommand reg = new RegisterCommand(aliases, desc, usage, executor, plugin);
            if (commandMap == null) {
                String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
                Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
                Object craftServerObject = craftServerClass.cast(plugin.getServer());
                Field commandMapField = craftServerClass.getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (CommandMap)commandMapField.get(craftServerObject);
            }
            commandMap.register(plugin.getDescription().getName(), reg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}