package net.liebleachxdot.tools.manager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.liebleachxdot.game.Arena;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Expansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "bleach";
    }

    @Override
    public @NotNull String getAuthor() {
        return "_a_k_u_l_k_a_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        Arena arena = Arena.get(player);
        if(arena == null) return null;

        if (params.equalsIgnoreCase("gotei_scores"))
            return String.valueOf(arena.getGoteiScores());
        if (params.equalsIgnoreCase("espada_scores"))
            return String.valueOf(arena.getEspadaScores());
        if (params.equalsIgnoreCase("max_scores"))
            return String.valueOf(arena.getMaxScores());
        return null;
    }
}
