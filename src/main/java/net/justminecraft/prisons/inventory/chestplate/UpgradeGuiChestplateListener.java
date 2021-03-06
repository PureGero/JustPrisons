package net.justminecraft.prisons.inventory.chestplate;

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

public class UpgradeGuiChestplateListener implements Listener {
    public UpgradeGuiChestplateListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UpgradeGuiChestplate) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof UpgradeGuiChestplate) {
                for (int i = 0; i < (event.isShiftClick() ? 10 : 1); i++) {
                    ((UpgradeGuiChestplate) event.getClickedInventory().getHolder()).onClick(event.getSlot());
                }
            }
        }
        
        if (event.getClick() == ClickType.RIGHT && event.getCurrentItem().getType()== Material.DIAMOND_CHESTPLATE) {
            Player player = (Player) event.getWhoClicked();
            new UpgradeGuiChestplate(player, event.getSlot());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.DIAMOND_CHESTPLATE
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            new UpgradeGuiChestplate(event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot());
            event.setCancelled(true);
        }
    }
}
