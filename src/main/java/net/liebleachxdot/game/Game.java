package net.liebleachxdot.game;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.api.Expansion;
import net.liebleachxdot.team.TeamModel;
import net.liebleachxdot.team.TeamPlayer;
import net.liebleachxdot.tools.Utility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {


    private final List<TeamPlayer> gamePlayers;
    private final ScoreboardManager scoreboardManager;
    private Scoreboard scoreboard;

    private Expansion expansion;

    private final Arena arena;

    public Game(Arena arena) {
        this.arena = arena;
        this.gamePlayers = new ArrayList<>();
        this.scoreboardManager = TabAPI.getInstance().getScoreboardManager();

        List<String> lines = LieBleachxDot.getInstance().getConfig().getStringList("arena.game.scoreboard.lines")
                .stream()
                .map(Utility::rgb)
                .collect(Collectors.toList());

        if (scoreboardManager != null) {
            this.scoreboard = scoreboardManager.createScoreboard("scoreboard_" + arena.getName(),
                    LieBleachxDot.getInstance().getConfig().getString("arena.game.scoreboard.title"), lines);
        }

        expansion = new Expansion();
        expansion.register();
        startClassGameCheck();
        startScoreUpdateTask();
    }

    public void match() {
        arena.setOpen(false);
        teleportPlayers();
    }

    public void teleportPlayers() {
        if (arena == null
                || arena.getPos1() == null
                || arena.getPos2() == null) {
            return;
        }

        Location loc1 = arena.getPos1().toBlockLocation();
        Location loc2 = arena.getPos2().toBlockLocation();

        for (TeamPlayer teamPlayer : gamePlayers) {
            Location targetLoc;
            if (teamPlayer.getTeamModel().getPickName().equalsIgnoreCase("gotei")) {
                targetLoc = loc1;
            } else {
                targetLoc = loc2;
            }

            Player player = teamPlayer.getPlayer();
            player.teleport(targetLoc);
            Utility.preparePlayer(player);
            Utility.grantAbilities(arena);
            setupScoreboard();
        }
    }

    private void setupScoreboard() {
        for (TeamPlayer player : gamePlayers) {
            scoreboardManager.showScoreboard(TabAPI.getInstance().getPlayer(player.getPlayer().getUniqueId()), scoreboard);
        }
    }

    public void resetScoreboards() {
        for (TeamPlayer player : gamePlayers) {
            scoreboardManager.resetScoreboard(TabAPI.getInstance().getPlayer(player.getPlayer().getUniqueId()));
        }
        if (scoreboard != null) {
            scoreboard.unregister();
        }
    }

    public void endGame(TeamModel teamModel) {
        if (teamModel == null) {
            return;
        }

        arena.sendArenaTitle("&aСхватка окончена! Победила команда - &6" + teamModel.getTeamName(), "");

        new BukkitRunnable() {
            @Override
            public void run() {
                arena.reset();
            }
        }.runTaskLater(LieBleachxDot.getInstance(), 10 * 20L);
    }

    private void startClassGameCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arena.isClassesChoose()) {
                    return;
                }

                boolean classesReady = arena.getYamomotoClass().getPlayers().size() == arena.getYamomotoClass().getMaxPlayers() &&
                        arena.getIchigoClass().getPlayers().size() == arena.getIchigoClass().getMaxPlayers() &&
                        arena.getUlquiorraClass().getPlayers().size() == arena.getUlquiorraClass().getMaxPlayers();

                if (classesReady) {
                    Bukkit.getScheduler().runTask(LieBleachxDot.getInstance(), () -> {
                        arena.setClassesChoose(true);
                        if (arena.getPlayers().size() >= arena.getMinPlayers() && !arena.isStarting()) {
                            arena.startTimer();
                        }
                    });
                }
            }
        }.runTaskTimerAsynchronously(LieBleachxDot.getInstance(), 0L, 20L);
    }

    private void startScoreUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkGameEndConditions();
            }
        }.runTaskTimerAsynchronously(LieBleachxDot.getInstance(), 0L, 20L);
    }

    private void checkGameEndConditions() {
        if (arena.getFirstTeam().getPlayers().isEmpty()) {
            if (arena.isStarting()) {
                Bukkit.getScheduler().runTask(LieBleachxDot.getInstance(), () -> endGame(arena.getSecondTeam()));
            }
            return;
        } else if (arena.getSecondTeam().getPlayers().isEmpty()) {
            if (arena.isStarting()) {
                Bukkit.getScheduler().runTask(LieBleachxDot.getInstance(), () -> endGame(arena.getFirstTeam()));
            }
            return;
        }

        if (arena.getEspadaScores() >= arena.getMaxScores()) {
            Bukkit.getScheduler().runTask(LieBleachxDot.getInstance(), () -> endGame(arena.getSecondTeam()));
        } else if (arena.getGoteiScores() >= arena.getMaxScores()) {
            Bukkit.getScheduler().runTask(LieBleachxDot.getInstance(), () -> endGame(arena.getFirstTeam()));
        }
    }

    public TeamPlayer getTeamPlayer(Player player) {
        return gamePlayers.stream()
                .filter(teamPlayer -> teamPlayer.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }

    public TeamModel getTeam(Player player) {
        return gamePlayers.stream()
                .filter(teamPlayer -> teamPlayer.getPlayer().equals(player))
                .map(TeamPlayer::getTeamModel)
                .findFirst()
                .orElse(null);
    }

    public List<TeamPlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Expansion getExpansionAPI() {
        return expansion;
    }
}
