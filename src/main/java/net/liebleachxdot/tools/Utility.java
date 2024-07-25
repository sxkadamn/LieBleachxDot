package net.liebleachxdot.tools;

import net.kyori.adventure.text.Component;
import net.liebleachxdot.LieBleachxDot;
import net.liebleachxdot.game.Arena;
import net.liebleachxdot.team.ClassSelection;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    private static final String signature = "&4[" + LieBleachxDot.getInstance().getName() + "] >>> ";
    private static final String errorSignature = "&4[" + LieBleachxDot.getInstance().getName() + " &cERROR] >>> ";

    public static void sendMessage(CommandSender player, String msg, boolean isError){
        if (isError)
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', errorSignature + "&6&l" + msg)));
        else
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', signature + "&6&l" + msg)));
    }


    public static void preparePlayer(Player player) {
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.setGameMode(GameMode.SURVIVAL);
    }

    public static void grantAbilities(Arena arena) {
        arena.getYamomotoClass().grantAbilities();
        arena.getIchigoClass().grantAbilities();
        arena.getUlquiorraClass().grantAbilities();
    }

    public static ItemStack createTeamSelector(ClassSelection selection) {
        ItemStack itemStack = new ItemStack(selection.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(rgb(selection.getDisplayName()));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static ItemStack createItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(rgb(displayName));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static String rgb(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            StringBuilder builder = new StringBuilder();
            for (char c : replaceSharp.toCharArray()) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace("&", "");
    }

}
