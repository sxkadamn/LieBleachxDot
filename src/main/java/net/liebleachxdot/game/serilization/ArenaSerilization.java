package net.liebleachxdot.game.serilization;

import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.game.Arena;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class ArenaSerilization {

    public static void loadFromConfig() {
        File folder = new File("plugins/" + LieBleachxDot.getInstance().getName() + "/arenas/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (Stream<java.nio.file.Path> paths = Files.walk(folder.toPath(), 1)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".yml"))
                    .forEach(path -> loadArenaFromFile(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadArenaFromFile(File file) {
        String arenaName = file.getName().replaceFirst("[.][^.]+$", "");
        Arena arena = Arena.add(arenaName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        arena.setPos1(yamlConfiguration.getLocation("pos1"));
        arena.setPos2(yamlConfiguration.getLocation("pos2"));
        arena.setLobby(yamlConfiguration.getLocation("lobby"));
        arena.setMinPlayers(yamlConfiguration.getInt("minPlayers"));
        arena.setTimeToStart(yamlConfiguration.getInt("timeToStart"));
        arena.setMaxScores(yamlConfiguration.getInt("maxScores"));
        arena.setMaxFirstTeam(yamlConfiguration.getInt("maxFirstTeam"));
        arena.setMaxSecondTeam(yamlConfiguration.getInt("maxSecondTeam"));

        arena.launch();
    }

    public static void writeToConfig() {
        Arena.getArenas().forEach(ArenaSerilization::writeArenaToFile);
    }

    private static void writeArenaToFile(Arena arena) {
        File file = new File("plugins/" + LieBleachxDot.getInstance().getName() + "/arenas/" + arena.getName() + ".yml");
        YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("pos1", arena.getPos1());
        configuration.set("pos2", arena.getPos2());
        configuration.set("lobby", arena.getLobby());
        configuration.set("minPlayers", arena.getMinPlayers());
        configuration.set("timeToStart", arena.getTimeToStart());
        configuration.set("maxScores", arena.getMaxScores());
        configuration.set("maxFirstTeam", arena.getMaxFirstTeam());
        configuration.set("maxSecondTeam", arena.getMaxSecondTeam());

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
