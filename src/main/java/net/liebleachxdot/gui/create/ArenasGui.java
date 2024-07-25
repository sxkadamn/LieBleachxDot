package net.liebleachxdot.gui.create;

import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.game.Arena;
import net.liebleachxdot.gui.Menu;
import net.liebleachxdot.gui.MenuSlot;
import net.liebleachxdot.gui.SlotListener;
import net.liebleachxdot.team.TeamPlayer;
import net.liebleachxdot.tools.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;


public class ArenasGui {

    public void chooseClassesGuiEspada(Player player, Arena arena) {
        if(arena == null) return;

        Menu menu = LieBleachxDot.getManager().createGUI("classChooseEspada", player.getName(), Utility.rgb("Выбор класса"), 6);
        MenuSlot ulquiorra = new MenuSlot(Material.BLUE_WOOL);
        ulquiorra.setDisplay(Utility.rgb("&b&lУлькиора - " + arena.getUlquiorraClass().getPlayers().size() + " / " + arena.getUlquiorraClass().getMaxPlayers()));
        ulquiorra.setListener(new SlotListener() {
            @Override
            public boolean onLeftClick(Player player) {
                if(arena.getUlquiorraClass().getPlayers().size() == arena.getIchigoClass().getMaxPlayers()) {
                    Utility.sendMessage(player, "&cКласс &4&lИчиго &cпереполнен!", true);
                    return true;
                }

                arena.getUlquiorraClass().addPlayer(player);
                player.closeInventory();
                return true;
            }
        });
        menu.setSlot(13, ulquiorra);
        player.openInventory(menu.getInventory());
    }

    public void chooseClassesGuiGotei(Player player, Arena arena) {
        if(arena == null) return;

        Menu menu = LieBleachxDot.getManager().createGUI("classChooseGotei", player.getName(), Utility.rgb("Выбор класса"), 6);
        MenuSlot ichigo = new MenuSlot(Material.BLACK_WOOL);
        ichigo.setDisplay(Utility.rgb("&4&lИчиго - " + arena.getIchigoClass().getPlayers().size() + " / " + arena.getIchigoClass().getMaxPlayers()));
        ichigo.setListener(new SlotListener() {
            @Override
            public boolean onLeftClick(Player player) {
                if(arena.getIchigoClass().getPlayers().size() == arena.getIchigoClass().getMaxPlayers()) {
                    Utility.sendMessage(player, "&cКласс &4&lИчиго &cпереполнен!", true);
                    return true;
                }

                arena.getIchigoClass().addPlayer(player);
                player.closeInventory();
                return true;
            }
        });
        MenuSlot yamomoto = new MenuSlot(Material.YELLOW_WOOL);
        yamomoto.setDisplay(Utility.rgb("&e&lЯмомото - " + arena.getYamomotoClass().getPlayers().size() + " / " + arena.getYamomotoClass().getMaxPlayers()));
        yamomoto.setListener(new SlotListener() {
            @Override
            public boolean onLeftClick(Player player) {
                if(arena.getYamomotoClass().getPlayers().size() == arena.getYamomotoClass().getMaxPlayers()) {
                    Utility.sendMessage(player, "&cКласс &e&lЯмомото &cпереполнен!", true);
                    return true;
                }

                arena.getYamomotoClass().addPlayer(player);
                player.closeInventory();
                return true;

            }
        });
        menu.setSlot(12, yamomoto);
        menu.setSlot(13, ichigo);
        player.openInventory(menu.getInventory());
    }

    public void continueGui(Player player, Arena arena) {
        if(arena == null) return;

        Menu menu = LieBleachxDot.getManager().createGUI("teamChoose", player.getName(), Utility.rgb("Выбор команды"), 6);
        MenuSlot espada = new MenuSlot(Material.BLUE_STAINED_GLASS_PANE);
        if(arena.isOpen()) {
            espada.setDisplay(Utility.rgb("&9Эспада - " + arena.getSecondTeam().getPlayers().size() + " / " + arena.getMaxSecondTeam()));
            espada.setListener(new SlotListener() {
                @Override
                public boolean onLeftClick(Player player) {
                    if(arena.getSecondTeam().getPlayers().size() == arena.getMaxSecondTeam()) {
                        Utility.sendMessage(player, "&6Команда &9Эспада уже заполнена!", true);
                        return true;
                    }
                    
                    if(arena.getFirstTeam().contains(player))
                        arena.getFirstTeam().removePlayer(player);

                    if (!arena.join(player)) Utility.sendMessage(player, "&cНевозможно присоединиться.", true);
                    arena.addPlayerToTeam(player, arena.getSecondTeam());
                    arena.getGame().getGamePlayers().add(new TeamPlayer(arena.getSecondTeam(), player));


                    return true;
                }
            });
        }
        MenuSlot gotei = new MenuSlot(Material.BLUE_STAINED_GLASS_PANE);
        if(arena.isOpen()) {
            gotei.setDisplay(Utility.rgb("&cГотей 13 - " + arena.getFirstTeam().getPlayers().size() + " / " + arena.getMaxFirstTeam()));
            gotei.setListener(new SlotListener() {
                @Override
                public boolean onLeftClick(Player player) {
                    if(arena.getFirstTeam().getPlayers().size() == arena.getMaxFirstTeam()) {
                        Utility.sendMessage(player, "&6Команда Готей 13 уже заполнена!", true);
                        return true;
                    }
                    if(arena.getSecondTeam().contains(player))
                        arena.getSecondTeam().removePlayer(player);

                    if (!arena.join(player)) Utility.sendMessage(player, "&cНевозможно присоединиться.", true);

                    arena.addPlayerToTeam(player, arena.getFirstTeam());
                    arena.getGame().getGamePlayers().add(new TeamPlayer(arena.getFirstTeam(), player));


                    return true;
                }
            });
        }
        menu.setSlot(14, gotei);
        menu.setSlot(12, espada);
        player.openInventory(menu.getInventory());
    }

    public void arenaGui(Player player) {

        Menu menu = LieBleachxDot.getManager().createGUI("arenas", player.getName(), Utility.rgb("Арены битв"), 6);

        for (Arena arena : Arena.getArenas()) {
            if (arena != null) {

                MenuSlot menuSlot = new MenuSlot(Material.GREEN_STAINED_GLASS_PANE);
                if (arena.isOpen()) {
                    menuSlot.setDisplay(Utility.rgb("&aСвободная арена " + arena.getPlayers().size() + " / " + arena.getMinPlayers()));
                    List<String> lore = LieBleachxDot.getInstance().getConfig().getStringList("arena.gui.lore").stream()
                            .map(s -> s.replaceAll("%gotei_team", String.valueOf(arena.getFirstTeam().getPlayers().size())))
                            .map(s -> s.replaceAll("%espada_team", String.valueOf(arena.getSecondTeam().getPlayers().size())))
                            .map(s -> s.replaceAll("%gotei_max", String.valueOf(arena.getMaxSecondTeam())))
                            .map(s -> s.replaceAll("%espada_max", String.valueOf(arena.getMaxSecondTeam())))
                            .map(Utility::rgb)
                            .collect(Collectors.toList());
                    menuSlot.setLoreList(lore);
                    menuSlot.setListener(new SlotListener() {
                        @Override
                        public boolean onLeftClick(Player player) {
                            continueGui(player, arena);
                            return true;
                        }
                    });
                } else {
                    menuSlot.setItem(new ItemStack(Material.RED_STAINED_GLASS_PANE));
                    menuSlot.setDisplay(Utility.rgb("&cАрена активна " + arena.getPlayers() + " / " + arena.getMinPlayers()));
                    List<String> lore = LieBleachxDot.getInstance().getConfig().getStringList("arena.gui.lore").stream()
                            .map(s -> s.replaceAll("%gotei_team", String.valueOf(arena.getFirstTeam().getPlayers().size())))
                            .map(s -> s.replaceAll("%espada_team", String.valueOf(arena.getSecondTeam().getPlayers().size())))
                            .map(s -> s.replaceAll("%gotei_max", String.valueOf(arena.getMaxSecondTeam())))
                            .map(s -> s.replaceAll("%espada_max", String.valueOf(arena.getMaxSecondTeam())))
                            .map(Utility::rgb)
                            .collect(Collectors.toList());
                    menuSlot.setLoreList(lore);

                    menuSlot.setListener(new SlotListener() {
                        @Override
                        public boolean onLeftClick(Player player) {
                            Utility.sendMessage(player, "&cНевозможно присоединиться. Арена переполнена!", true);
                            return true;
                        }
                    });
                }

                int firstEmptySlot = -1;
                for (int i = 11; i < menu.getInventory().getSize(); i++) {
                    if (menu.getInventory().getItem(i) == null) {
                        firstEmptySlot = i;
                        break;
                    }
                }

                if (firstEmptySlot != -1) {
                    menu.setSlot(firstEmptySlot, menuSlot);
                }
                player.openInventory(menu.getInventory());
            }
        }
    }
}
