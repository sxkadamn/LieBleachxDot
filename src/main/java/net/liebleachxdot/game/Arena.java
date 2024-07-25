package net.liebleachxdot.game;

import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.game.classes.IchigoClass;
import net.liebleachxdot.game.classes.UlquiorraClass;
import net.liebleachxdot.game.classes.YamomotoClass;
import net.liebleachxdot.team.TeamModel;
import net.liebleachxdot.team.TeamPlayer;
import net.liebleachxdot.tools.Utility;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena {

    private static final List<Arena> arenaList = new ArrayList<>();

    private final String name;
    private Location pos1, pos2;
    private Location lobby;
    private int minPlayers;
    private int timeToStart;

    private final TeamModel firstTeam;
    private final TeamModel secondTeam;

    private int maxFirstTeam;
    private int maxSecondTeam;
    private int maxScores;
    private int goteiScores = 0;
    private int espadaScores = 0;

    private final YamomotoClass yamomotoClass;
    private final IchigoClass ichigoClass;

    private final UlquiorraClass ulquiorraClass;

    private final List<Player> players = new ArrayList<>();
    private final Map<Player, Location> onJoinLocation = new HashMap<>();

    private boolean isOpen = false;
    private boolean isStarting = false;

    private boolean isClassesChoose = false;

    private final Game game;

    public Arena(String name) {
        this.name = name;
        this.game = new Game(this);
        this.yamomotoClass = new YamomotoClass();
        this.ichigoClass = new IchigoClass();
        this.ulquiorraClass = new UlquiorraClass();
        this.firstTeam = new TeamModel("&cГотей 13", "gotei");
        this.secondTeam = new TeamModel("&9Эспада", "espada");

        registerEventListeners();

        arenaList.add(this);
    }

    private void registerEventListeners() {
        LieBleachxDot.getInstance().getServer().getPluginManager().registerEvents(yamomotoClass, LieBleachxDot.getInstance());
        LieBleachxDot.getInstance().getServer().getPluginManager().registerEvents(ichigoClass, LieBleachxDot.getInstance());
    }

    public void reset() {
        players.forEach(player -> {
            Location location = onJoinLocation.get(player);

            if (location == null) {
                return;
            }

            player.teleport(location);
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            ItemStack itemStack = Utility.createItem(
                    Material.DIAMOND,
                    "&6Выбор арены <--- CLICK");
            player.getInventory().addItem(itemStack);
        });

        game.resetScoreboards();
        game.getExpansionAPI().unregister();

        onJoinLocation.clear();
        ichigoClass.getPlayers().clear();
        yamomotoClass.getPlayers().clear();
        ulquiorraClass.getPlayers().clear();

        firstTeam.getPlayers().clear();
        secondTeam.getPlayers().clear();

        isOpen = true;
        isStarting = false;
        isClassesChoose = false;
    }

    public boolean join(Player player) {
        if (get(player) != null || !isOpen) {
            return true;
        }

        players.add(player);
        onJoinLocation.put(player, player.getLocation());
        player.teleport(lobby);


        return true;
    }

    public void leave(Player player) {
        ichigoClass.getPlayers().remove(player);
        yamomotoClass.getPlayers().remove(player);
        ulquiorraClass.getPlayers().remove(player);

        TeamPlayer teamPlayer = game.getTeamPlayer(player);
        if (teamPlayer != null) {
            if (teamPlayer.getTeamModel().getPickName().equalsIgnoreCase("gotei")) {
                removePlayerFromTeam(player, firstTeam);
            } else if (teamPlayer.getTeamModel().getPickName().equalsIgnoreCase("espada")) {
                removePlayerFromTeam(player, secondTeam);
            }
            game.getGamePlayers().remove(teamPlayer);
        }

        if (players.remove(player)) {
            player.teleport(onJoinLocation.get(player));
            onJoinLocation.remove(player);
        }
        Utility.preparePlayer(player);
    }

    public void startTimer() {
        setStarting(true);

        new BukkitRunnable() {
            int ctr = timeToStart;

            @Override
            public void run() {
                sendArenaTitle("До начала осталось - " + ctr + "!", "Приготовься");
                playArenaSound(Sound.BLOCK_NOTE_BLOCK_PLING);

                if (players.size() < minPlayers) {
                    sendArenaMessage("Недостаточно игроков");
                    playArenaSound(Sound.ENTITY_VILLAGER_NO);
                    setStarting(false);
                    cancel();
                    return;
                }

                if (--ctr == 0) {
                    game.match();
                    cancel();
                }
            }
        }.runTaskTimer(LieBleachxDot.getInstance(), 0L, 20L);
    }

    public void sendArenaMessage(String msg) {
        players.forEach(player -> Utility.sendMessage(player, msg, false));
    }

    public void sendArenaTitle(String msg, String subMsg) {
        players.forEach(player -> player.sendTitle(msg, subMsg, 10, 40, 10));
    }

    public void playArenaSound(Sound sound) {
        players.forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
    }

    public boolean launch() {
        return pos1 != null && pos2 != null && pos1.distance(pos2) >= 100 && (isOpen = true);
    }

    public static Arena add(String name) {
        return new Arena(name);
    }

    public static Arena get(Player player) {
        return arenaList.stream().filter(arena -> arena.getPlayers().contains(player)).findFirst().orElse(null);
    }

    public static Arena get(String name) {
        return arenaList.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getTimeToStart() {
        return timeToStart;
    }

    public void setTimeToStart(int timeToStart) {
        this.timeToStart = timeToStart;
    }

    public int getMaxFirstTeam() {
        return maxFirstTeam;
    }

    public void setMaxFirstTeam(int maxFirstTeam) {
        this.maxFirstTeam = maxFirstTeam;
    }

    public int getMaxSecondTeam() {
        return maxSecondTeam;
    }

    public void setMaxSecondTeam(int maxSecondTeam) {
        this.maxSecondTeam = maxSecondTeam;
    }

    public int getMaxScores() {
        return maxScores;
    }

    public void setMaxScores(int maxScores) {
        this.maxScores = maxScores;
    }

    public int getGoteiScores() {
        return goteiScores;
    }

    public void setGoteiScores(int goteiScores) {
        this.goteiScores = goteiScores;
    }

    public int getEspadaScores() {
        return espadaScores;
    }

    public void setEspadaScores(int espadaScores) {
        this.espadaScores = espadaScores;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Player, Location> getOnJoinLocation() {
        return onJoinLocation;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean isStarting) {
        this.isStarting = isStarting;
    }

    public boolean isClassesChoose() {
        return isClassesChoose;
    }

    public void setClassesChoose(boolean classesChoose) {
        isClassesChoose = classesChoose;
    }

    public Game getGame() {
        return game;
    }

    public static List<Arena> getArenas() {
        return arenaList;
    }

    public YamomotoClass getYamomotoClass() {
        return yamomotoClass;
    }

    public IchigoClass getIchigoClass() {
        return ichigoClass;
    }

    public UlquiorraClass getUlquiorraClass() {
        return ulquiorraClass;
    }

    public TeamModel getFirstTeam() {
        return firstTeam;
    }

    public TeamModel getSecondTeam() {
        return secondTeam;
    }

    public void addPlayerToTeam(Player player, TeamModel team) {
        team.addPlayer(player);
    }

    public void removePlayerFromTeam(Player player, TeamModel team) {
        team.removePlayer(player);
    }


}
