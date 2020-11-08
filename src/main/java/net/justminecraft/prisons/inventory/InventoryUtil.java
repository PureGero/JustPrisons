package net.justminecraft.prisons.inventory;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    /**
     * Add items to the player's inventory, or throw them on the ground if the
     * player's inventory is full
     */
    public static void giveItem(Player player, ItemStack... items) {
        for (ItemStack itemStack : player.getInventory().addItem(items).values()) {
            Item item = player.getWorld().dropItem(player.getEyeLocation(), itemStack);
            item.setVelocity(player.getEyeLocation().getDirection().multiply(0.3));
        }
    }
}
