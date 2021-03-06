package net.justminecraft.prisons.inventory.pickaxe;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PickaxeTimer implements Runnable {

    public PickaxeTimer(PrisonsPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 10, 10);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack item = player.getItemInHand();
            if (item == null) continue;

            int speedBoost = UpgradePickaxe.getLevel(item, UpgradePickaxe.SPEED_BOOST).intValue();

            if (speedBoost > 3) {
                speedBoost = 3;
            }

            if (speedBoost > 0) {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speedBoost - 1, true, false));
            }
        }
    }
}
