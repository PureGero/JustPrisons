package net.justminecraft.prisons.inventory.pickaxe;

import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.inventory.InventoryUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;

public class Pickaxe {
    public static void giveEpicPickaxe(Player player) {
        ItemStack pick = generatePickaxe(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Epic Pickaxe");
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.EFFICIENCY, BigInteger.valueOf((long) (Math.random() * 500 + 100)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.FORTUNE, BigInteger.valueOf((long) (Math.random() * 500 + 100)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.SPEED_BOOST, BigInteger.valueOf(3));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.LUCK, BigInteger.valueOf((long) (Math.random() * 8 + 4)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.LOOTING, BigInteger.valueOf((long) (Math.random() * 300 + 100)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.LURE, BigInteger.valueOf((long) (Math.random() * 2 + 1)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.UNBREAKING, BigInteger.valueOf(2));
        givePickaxe(player, pick);
    }

    public static void giveLegendPickAxe(Player player) {
        ItemStack pick = generatePickaxe(ChatColor.GOLD + "" + ChatColor.BOLD + "Legendary Pickaxe");
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.EFFICIENCY, BigInteger.valueOf((long) (Math.random() * 1000 + 100)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.FORTUNE, BigInteger.valueOf((long) (Math.random() * 1000 + 100)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.SPEED_BOOST, BigInteger.valueOf(3));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.LUCK, BigInteger.valueOf((long) (Math.random() * 10 + 5)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.LOOTING, BigInteger.valueOf((long) (Math.random() * 500 + 100)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.LURE, BigInteger.valueOf((long) (Math.random() * 4 + 2)));
        UpgradePickaxe.setUpgrade(pick, UpgradePickaxe.UNBREAKING, BigInteger.valueOf(3));
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
