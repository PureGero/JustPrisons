package net.justminecraft.prisons.inventory.blocks;

import net.justminecraft.prisons.GoogleSheet;
import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockLoot extends GoogleSheet {

    public BlockLoot(PrisonsPlugin plugin) {
        super(plugin, "1taCMHuSTUm1Buxfdk8r0AkKyMdNXfcSlyx-G4W2oJT8");
    }

    @Override
    public void onContentsChange() {
        // NAME (Material.valueOf), RARITY (Double.parseDouble)
        
        // TODO Parse csv file
    }

    private static void giveBlock(Player player, double rarityPower) {
        // rarity = Math.pow(rarity, rarityPower)

        // TODO Calculate appropriate block
        
        InventoryUtil.giveItem(player, new ItemStack(Material.GLASS));
    }
    
    public static void giveCommonBlock(Player player) {
        if (Math.random() < 0.1) {
            giveBlock(player, 1);
        }
    }

    public static void giveRareBlock(Player player) {
        if (Math.random() < 0.1) {
            giveBlock(player, 0.5);
        }
    }

    public static void giveEpicBlock(Player player) {
        if (Math.random() < 0.1) {
            giveBlock(player, 0.25);
        }
    }

    public static void giveLegendaryBlock(Player player) {
        if (Math.random() < 0.1) {
            giveBlock(player, 0);
        }
    }
}
