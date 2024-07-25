package net.liebleachxdot.team;

import org.bukkit.Material;

public enum ClassSelection {
    GOTEI("&cГотей 13", Material.ORANGE_BANNER, "&6Выбор класса (&cГотей 13) <--- CLICK"),
    ESPADA("&9Эспада", Material.BLUE_BANNER, "&6Выбор класса (&9Эспада) <--- CLICK");

    private final String teamName;
    private final Material material;
    private final String displayName;

    ClassSelection(String teamName, Material material, String displayName) {
        this.teamName = teamName;
        this.material = material;
        this.displayName = displayName;
    }

    public String getTeamName() {
        return teamName;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ClassSelection fromName(String name) {
        for (ClassSelection selection : values()) {
            if (selection.getTeamName().equalsIgnoreCase(name)) {
                return selection;
            }
        }
        return null;
    }
}
