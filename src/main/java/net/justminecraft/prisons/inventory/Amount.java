package net.justminecraft.prisons.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Amount {
    ONE("1", ChatColor.BLUE),
    FIVE("5", ChatColor.BLUE, "prisons.amount.five"),
    TEN("10", ChatColor.BLUE, "prisons.amount.ten"),
    ONE_HUNDRED("100", ChatColor.BLUE, "prisons.amount.one_hundred"),
    ONE_THOUSAND("1000", ChatColor.BLUE, "prisons.amount.one_thousand"),
    MAX("Max", ChatColor.BLUE, "prisons.amount.max");

    private final String name;
    private final String description;
    private final ChatColor color;
    private final String permission;

    Amount(String name, ChatColor color) {
        this.name = name;
        this.description = "Get " + name + " upgrades per click!";
        this.color = color;
        this.permission = null;
    }

    Amount(String name, ChatColor color, String permission) {
        this.name = name;
        this.description = "Get " + name + " upgrades per click!";
        this.color = color;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getPermission() {
        return permission;
    }

    public Material getIcon() {
        switch (this) {
            case MAX:
                return Material.EMERALD_BLOCK;
            default:
                return Material.GOLD_BLOCK;
        }
    }
}
