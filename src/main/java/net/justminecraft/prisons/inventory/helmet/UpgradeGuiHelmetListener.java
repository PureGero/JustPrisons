package net.justminecraft.prisons.inventory.helmet;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.boots.UpgradeGuiBoots;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UpgradeGuiHelmetListener implements Listener {
    public UpgradeGuiHelmetListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UpgradeGuiHelmet) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof UpgradeGuiHelmet) {
                for (int i = 0; i < (event.isShiftClick() ? 10 : 1); i++) {
                    ((UpgradeGuiHelmet) event.getClickedInventory().getHolder()).onClick(event.getSlot());
                }
            }
        }
        
        if (event.getClick() == ClickType.RIGHT && event.getCurrentItem().getType()== Material.DIAMOND_HELMET) {
            Player player = (Player) event.getWhoClicked();
            new UpgradeGuiHelmet(player, event.getSlot());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.DIAMOND_HELMET
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            new UpgradeGuiHelmet(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot());
            event.setCancelled(true);
        }
    }
}
