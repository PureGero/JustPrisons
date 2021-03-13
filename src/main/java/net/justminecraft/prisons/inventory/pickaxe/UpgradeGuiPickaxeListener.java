package net.justminecraft.prisons.inventory.pickaxe;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UpgradeGuiPickaxeListener implements Listener {
    public UpgradeGuiPickaxeListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UpgradeGuiPickaxe) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof UpgradeGuiPickaxe) {
                for (int i = 0; i < (event.isShiftClick() ? 10 : 1) * (PlayerDataManager.get((Player) event.getWhoClicked()).getUpgradeAmount() == 2 ? 100000000 : PlayerDataManager.get((Player) event.getWhoClicked()).getUpgradeAmount()); i++) {
                    ((UpgradeGuiPickaxe) event.getClickedInventory().getHolder()).onClick(event.getSlot());
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.DIAMOND_PICKAXE
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            new UpgradeGuiPickaxe(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot());
            event.setCancelled(true);
        }
    }
}
