package net.justminecraft.prisons.inventory.boots;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UpgradeGuiBootsListener implements Listener {
    public UpgradeGuiBootsListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UpgradeGuiBoots) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof UpgradeGuiBoots) {
                for (int i = 0; i < (event.isShiftClick() ? 10 : 1); i++) {
                    ((UpgradeGuiBoots) event.getClickedInventory().getHolder()).onClick(event.getSlot());
                }
            }
        }
        
        if (event.getClick() == ClickType.RIGHT && event.getCurrentItem().getType()== Material.DIAMOND_BOOTS) {
            Player player = (Player) event.getWhoClicked();
            new UpgradeGuiBoots(player, event.getSlot());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.DIAMOND_BOOTS
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            new UpgradeGuiBoots(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot());
            event.setCancelled(true);
        }
        
    }
}
