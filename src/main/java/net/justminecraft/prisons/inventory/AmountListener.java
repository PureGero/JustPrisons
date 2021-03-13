package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AmountListener implements Listener {
    public AmountListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof AmountGui) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof AmountGui) {
                ((AmountGui) event.getClickedInventory().getHolder()).onClick(event.getSlot());
            }
        }
    }
}
