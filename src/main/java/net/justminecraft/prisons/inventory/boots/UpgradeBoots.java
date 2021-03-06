package net.justminecraft.prisons.inventory.boots;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.justminecraft.prisons.mines.MineListener;

public enum UpgradeBoots {
    PROTECTION("Protection", "Increases protection against mobs", ChatColor.LIGHT_PURPLE, Enchantment.PROTECTION_ENVIRONMENTAL),
    BLAST_PROTECTION("Blast Protection", "Increases protection against explosions", ChatColor.DARK_RED, Enchantment.PROTECTION_EXPLOSIONS),
    FIRE_PROTECTION("Fire Protection", "Increases protection againt fire", ChatColor.GOLD, Enchantment.PROTECTION_FIRE),
    PROJECTILE_PROTECTION("Projectile Protection", "Increases protection against projectiles", ChatColor.GREEN, Enchantment.PROTECTION_PROJECTILE),
    UNBREAKING("Unbreaking", "Increases the durability of the boots", ChatColor.DARK_BLUE, Enchantment.DURABILITY),
    THORNS("Thorns", "Increases damage dealt to mobs on attack", ChatColor.DARK_AQUA, Enchantment.THORNS),
    RANKUP_TOKENS("Rankup tokens", "An internal enchantment that determines how many tokens should be received", ChatColor.GOLD);

    private final String name;
    private final String description;
    private final ChatColor color;
    private final Enchantment enchantment;

    UpgradeBoots(String name, String description, ChatColor color) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.enchantment = null;
    }

    UpgradeBoots(String name, String description, ChatColor color, Enchantment enchantment) {
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
            case PROTECTION:
                scalar = 2;
                start = 100;
                break;
            case BLAST_PROTECTION:
                scalar = 2;
                start = 100;
                break;
            case FIRE_PROTECTION:
                scalar = 2;
                start = 100;
                break;
            case PROJECTILE_PROTECTION:
                scalar = 2;
                start = 100;
                break;
            case UNBREAKING:
                scalar = 1;
                start = 10;
                break;
            case THORNS:
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
            case PROTECTION:
                return Material.BEDROCK;
            case BLAST_PROTECTION:
                return Material.TNT;
            case FIRE_PROTECTION:
                return Material.TORCH;
            case PROJECTILE_PROTECTION:
                return Material.ARROW;
            case UNBREAKING:
                return Material.ANVIL;
            case THORNS:
                return Material.DEAD_BUSH;
            default:
                return Material.STONE;
        }
    }

    public static BigInteger getLevel(ItemStack item, UpgradeBoots upgrade) {
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

    public static void setUpgrade(ItemStack item, UpgradeBoots upgrade, BigInteger level) {
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
