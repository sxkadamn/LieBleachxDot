package net.liebleachxdot.api;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

public abstract class BaseCommand implements CommandExecutor {
    private final String name;

    private final String[] usages;

    public BaseCommand(String name, String... aliases) {
        this.name = name;
        this.usages = aliases;
    }

    public String[] getAliases() {
        return this.usages;
    }

    public String getName() {
        return this.name;
    }

    public static void register(Plugin plugin, BaseCommand... executors) {
        for (BaseCommand executor : executors) {
            List<String> list = new ArrayList<>();
            Collections.addAll(list, executor.getAliases());
            list.add(executor.getName());
            RegisterCommand.reg(plugin, executor.getCommandExecutor(), list, "i love Mama <3, thanks for API", executor.getName());
        }
    }

    public CommandExecutor getCommandExecutor() {
        return this;
    }

    public abstract void execute(CommandSender paramCommandSender, String[] paramArrayOfString);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            execute(sender, args);
        } catch (ClassCastException e) {
            sender.sendMessage("NonNull");
        }
        return false;
    }
}
