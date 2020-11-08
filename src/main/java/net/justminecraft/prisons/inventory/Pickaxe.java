package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.Translate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;

public class Pickaxe {
    public static void giveEpicPickaxe(Player player) {
        ItemStack pick = generatePickaxe(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Epic Pickaxe");
        Upgrade.setUpgrade(pick, Upgrade.EFFICIENCY, BigInteger.valueOf((long) (Math.random() * 500 + 100)));
        Upgrade.setUpgrade(pick, Upgrade.FORTUNE, BigInteger.valueOf((long) (Math.random() * 500 + 100)));
        Upgrade.setUpgrade(pick, Upgrade.SPEED_BOOST, BigInteger.valueOf(3));
        Upgrade.setUpgrade(pick, Upgrade.LUCK, BigInteger.valueOf((long) (Math.random() * 8 + 4)));
        Upgrade.setUpgrade(pick, Upgrade.LOOTING, BigInteger.valueOf((long) (Math.random() * 300 + 100)));
        Upgrade.setUpgrade(pick, Upgrade.LURE, BigInteger.valueOf((long) (Math.random() * 2 + 1)));
        Upgrade.setUpgrade(pick, Upgrade.UNBREAKING, BigInteger.valueOf(2));
        givePickaxe(player, pick);
    }

    public static void giveLegendPickAxe(Player player) {
        ItemStack pick = generatePickaxe(ChatColor.GOLD + "" + ChatColor.BOLD + "Legendary Pickaxe");
        Upgrade.setUpgrade(pick, Upgrade.EFFICIENCY, BigInteger.valueOf((long) (Math.random() * 1000 + 100)));
        Upgrade.setUpgrade(pick, Upgrade.FORTUNE, BigInteger.valueOf((long) (Math.random() * 1000 + 100)));
        Upgrade.setUpgrade(pick, Upgrade.SPEED_BOOST, BigInteger.valueOf(3));
        Upgrade.setUpgrade(pick, Upgrade.LUCK, BigInteger.valueOf((long) (Math.random() * 10 + 5)));
        Upgrade.setUpgrade(pick, Upgrade.LOOTING, BigInteger.valueOf((long) (Math.random() * 500 + 100)));
        Upgrade.setUpgrade(pick, Upgrade.LURE, BigInteger.valueOf((long) (Math.random() * 4 + 2)));
        Upgrade.setUpgrade(pick, Upgrade.UNBREAKING, BigInteger.valueOf(3));
        givePickaxe(player, pick);
    }

    private static ItemStack generatePickaxe(String name) {
        ItemStack pick = new ItemStack(Material.GOLD_PICKAXE);
        ItemMeta meta = pick.getItemMeta();
        meta.setDisplayName(name);
        pick.setItemMeta(meta);
        return pick;
    }

    private static void givePickaxe(Player player, ItemStack pick) {
        ItemMeta meta = pick.getItemMeta();

        InventoryUtil.giveItem(player, pick);

        Translate.sendMessage(player, "prisons.keys.receive", meta.getDisplayName().substring(0, 2), meta.getDisplayName());
    }
}
