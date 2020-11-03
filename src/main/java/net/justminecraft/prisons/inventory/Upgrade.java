package net.justminecraft.prisons.inventory;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public enum Upgrade {
    EFFICIENCY("Efficiency", ChatColor.LIGHT_PURPLE, Enchantment.DIG_SPEED),
    FORTUNE("Fortune", ChatColor.GRAY, Enchantment.LOOT_BONUS_BLOCKS),
    UNBREAKING("Unbreaking", ChatColor.DARK_BLUE, Enchantment.DURABILITY),
    LUCK("Luck", ChatColor.DARK_GREEN),
    LOOTING("Looting", ChatColor.DARK_RED),
    CHARITY("Charity", ChatColor.GREEN),
    LURE("Lure", ChatColor.DARK_AQUA),
    SPEED_BOOST("Speed boost", ChatColor.GOLD);

    private final String name;
    private final ChatColor color;
    private final Enchantment enchantment;

    Upgrade(String name, ChatColor color) {
        this.name = name;
        this.color = color;
        this.enchantment = null;
    }

    Upgrade(String name, ChatColor color, Enchantment enchantment) {
        this.name = name;
        this.color = color;
        this.enchantment = enchantment;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public static BigInteger getLevel(ItemStack item, Upgrade upgrade) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        if (lore == null) {
            return BigInteger.ZERO;
        }

        for (String line : lore) {
            if (line.contains(upgrade.getName())) {
                return new BigInteger(line.replaceAll("\\D+",""));
            }
        }

        return BigInteger.ZERO;
    }

    public static void setUpgrade(ItemStack item, Upgrade upgrade, BigInteger level) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.removeIf(line -> line.contains(upgrade.getName()));

        lore.add(String.format("%s +%d %s", upgrade.getColor(), level, upgrade.getName()));

        meta.setLore(lore);

        item.setItemMeta(meta);

        int enchantmentLevel = level.intValue();

        if (level.compareTo(BigInteger.valueOf(32767)) > 0) {
            enchantmentLevel = 32767;
        }

        if (upgrade.getEnchantment() != null) {
            item.addUnsafeEnchantment(upgrade.getEnchantment(), enchantmentLevel);
        }
    }
}
