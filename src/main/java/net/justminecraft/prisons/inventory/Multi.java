package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.Translate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class Multi {
    public static void giveMulti(int multi, Player player) {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + multi + "x Multi");
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.LIGHT_PURPLE + "x" + multi + " multiplier for 15 minutes")));
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, multi);

        if (player.getInventory().addItem(item).size() == 0) {
            Translate.sendMessage(player, "prisons.keys.receive", meta.getDisplayName().substring(0, 2), meta.getDisplayName());
        }
    }
}
