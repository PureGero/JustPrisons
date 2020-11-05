package net.justminecraft.prisons.inventory;

import io.github.miraclefoxx.math.BigDecimalMath;
import net.justminecraft.prisons.mines.MineListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public enum Upgrade {
    FORTUNE("Fortune", "Increases the coins you get from mining", ChatColor.GRAY, Enchantment.LOOT_BONUS_BLOCKS),
    LUCK("Luck", "Increases the chance of finding tokens while mining", ChatColor.DARK_GREEN),
    EFFICIENCY("Efficiency", "Increases block breaking speed", ChatColor.LIGHT_PURPLE, Enchantment.DIG_SPEED),
    LOOTING("Looting", "Increases the amount of tokens you get", ChatColor.DARK_RED),
    SPEED_BOOST("Speed boost", "Increases your walking speed while holding the pickaxe", ChatColor.GOLD),
    CHARITY("Charity", "A chance of finding a huge sum of tokens", ChatColor.GREEN),
    UNBREAKING("Unbreaking", "Increases the durability of the pickaxe", ChatColor.DARK_BLUE, Enchantment.DURABILITY),
    LURE("Lure", "Increases the chance of finding keys while mining", ChatColor.DARK_AQUA),
    RANKUP_TOKENS("Rankup tokens", "An internal enchantment that determines how many tokens should be received", ChatColor.GOLD);

    private final String name;
    private final String description;
    private final ChatColor color;
    private final Enchantment enchantment;

    Upgrade(String name, String description, ChatColor color) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.enchantment = null;
    }

    Upgrade(String name, String description, ChatColor color, Enchantment enchantment) {
        this.name = name;
        this.description = description;
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

    public BigInteger getCost(BigInteger level) {
        int scalar = 0;
        int start = 0;
        switch (this) {
            case FORTUNE:
                scalar = 2;
                start = 100;
                break;
            case LUCK:
                scalar = 5000;
                start = 5000;
                break;
            case EFFICIENCY:
                scalar = 1;
                start = 10;
                break;
            case LOOTING:
                scalar = 50;
                start = 100;
                break;
            case SPEED_BOOST:
                scalar = 5000;
                start = 5000;
                break;
            case CHARITY:
                return BigDecimalMath.pow(BigDecimal.valueOf(2), new BigDecimal(level)).multiply(BigDecimal.valueOf(100000 * MineListener.TOKEN_MULTIPLIER)).toBigInteger();
            case UNBREAKING:
                scalar = 1;
                start = 10;
                break;
            case LURE:
                scalar = 10000;
                start = 10000;
                break;
        }
        return level
                .multiply(BigInteger.valueOf(scalar))
                .add(BigInteger.valueOf(start))
                .multiply(BigInteger.valueOf(MineListener.TOKEN_MULTIPLIER));
    }

    public Material getIcon() {
        switch (this) {
            case FORTUNE:
                return Material.GOLD_NUGGET;
            case LUCK:
                return Material.BOOK;
            case EFFICIENCY:
                return Material.GOLD_PICKAXE;
            case LOOTING:
                return Material.EMERALD_BLOCK;
            case SPEED_BOOST:
                return Material.FEATHER;
            case CHARITY:
                return Material.EMERALD_ORE;
            case UNBREAKING:
                return Material.ANVIL;
            case LURE:
                return Material.TRIPWIRE_HOOK;
            default:
                return Material.STONE;
        }
    }

    public static BigInteger getLevel(ItemStack item, Upgrade upgrade) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return BigInteger.ZERO;
        }

        List<String> lore = meta.getLore();

        if (lore == null) {
            return BigInteger.ZERO;
        }

        for (String line : lore) {
            if (line.contains(upgrade.getName())) {
                return new BigInteger(line.substring(2).replaceAll("\\D+",""));
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

    public String getDescription() {
        return description;
    }
}
