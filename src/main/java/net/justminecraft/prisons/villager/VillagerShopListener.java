package net.justminecraft.prisons.villager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class VillagerShopListener implements Listener {
    private final VillagerShopManager manager;

    public VillagerShopListener(VillagerShopManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (manager.getVillagerShop(event.getEntity()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (manager.getVillagerShop(event.getRightClicked()) != null) {
            manager.getVillagerShop(event.getRightClicked()).openShop(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof ClickableInventoryHolder) {
            ((ClickableInventoryHolder) event.getInventory().getHolder()).onClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof ClickableInventoryHolder) {
            ((ClickableInventoryHolder) event.getInventory().getHolder()).onClose(event);
        }
    }
}
