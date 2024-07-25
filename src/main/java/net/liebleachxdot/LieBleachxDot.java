package net.liebleachxdot;

import net.liebleachxdot.commands.AdminCommands;
import net.liebleachxdot.commands.UserCommands;
import net.liebleachxdot.game.classes.IchigoClass;
import net.liebleachxdot.game.classes.YamomotoClass;
import net.liebleachxdot.game.listeners.ArenaListener;
import net.liebleachxdot.game.listeners.GameListener;
import net.liebleachxdot.game.serilization.ArenaSerilization;
import net.liebleachxdot.gui.impl.MenuListener;
import net.liebleachxdot.gui.impl.MenuManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class LieBleachxDot extends JavaPlugin {

    private static LieBleachxDot instance;

    private static MenuManager manager;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getCommand("arena").setExecutor(new AdminCommands());
        getCommand("bleach").setExecutor(new UserCommands());


        checkPlugin("PlaceholderAPI");
        checkPlugin("TAB");

        manager = new MenuManager();

        getServer().getPluginManager().registerEvents(new ArenaListener(), this);
        getServer().getPluginManager().registerEvents(new GameListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        ArenaSerilization.loadFromConfig();
    }

    @Override
    public void onDisable() {
        ArenaSerilization.writeToConfig();
    }

    private void checkPlugin(String pluginName) {
        if (!getServer().getPluginManager().isPluginEnabled(pluginName)) {
            getLogger().severe("Плагин " + pluginName + " не воркает и я не воркаю(");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static LieBleachxDot getInstance() {
        return instance;
    }

    public static MenuManager getManager() {
        return manager;
    }
}
