package net.justminecraft.prisons.inventory.sword;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UpgradeGuiSwordListener implements Listener {
    public UpgradeGuiSwordListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UpgradeGuiSword) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof UpgradeGuiSword) {
                for (int i = 0; i < (event.isShiftClick() ? 10 : 1); i++) {
                    ((UpgradeGuiSword) event.getClickedInventory().getHolder()).onClick(event.getSlot());
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.DIAMOND_SWORD
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            new UpgradeGuiSword(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot());
            event.setCancelled(true);
        }
    }
}
