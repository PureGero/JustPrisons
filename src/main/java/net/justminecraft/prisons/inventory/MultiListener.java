package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class MultiListener implements Listener {

    public MultiListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            int level = event.getItem().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            if (level > 0) {
                PlayerData data = PlayerDataManager.get(event.getPlayer());
                data.startMulti(level, 15 * 60 * 1000);
                Translate.sendMessage(event.getPlayer(), "prisons.multi.start", level, 15);
                event.setCancelled(true);

                ItemStack item = event.getItem();
                item.setAmount(item.getAmount() - 1);
                event.getPlayer().setItemInHand(item);
            }
        }
    }
}
