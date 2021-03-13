package net.justminecraft.prisons.inventory.pickaxe;

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

public enum Upgrade2Pickaxe {

    private final String name;
    private final String description;
    private final ChatColor color;
    private final Enchantment enchantment;

    Upgrade2Pickaxe(String name, String description, ChatColor color) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.enchantment = null;
    }

    Upgrade2Pickaxe(String name, String description, ChatColor color, Enchantment enchantment) {
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
            case MULTI_MINE:
                return BigDecimalMath.pow(BigDecimal.valueOf(2), new BigDecimal(level)).multiply(BigDecimal.valueOf(500000 * MineListener.TOKEN_MULTIPLIER)).toBigInteger();
        }
        return level
                .multiply(BigInteger.valueOf(scalar))
                .add(BigInteger.valueOf(start))
                .multiply(BigInteger.valueOf(MineListener.TOKEN_MULTIPLIER));
    }

    public Material getIcon() {
        switch (this) {
            case MULTI_MINE:
                return Material.ARROW;
            default:
                return Material.STONE;
        }
    }

    public static BigInteger getLevel(ItemStack item, Upgrade2Pickaxe upgrade) {
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

    public static void setUpgrade(ItemStack item, Upgrade2Pickaxe upgrade, BigInteger level) {
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
