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
        if (event.getInventory().getHolder() instanceof UpgradeGuiPickaxe || event.getInventory().getHolder() instanceof UpgradeGui2Pickaxe) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null) {
                Player player = (Player) event.getWhoClicked();
                PlayerData playerData = PlayerDataManager.get(player);
                int multiplier = playerData.getUpgradeAmount() == 2 ? 100000000 : playerData.getUpgradeAmount();

                if(event.getClickedInventory().getHolder() instanceof UpgradeGuiPickaxe) {
                    for (int i = 0; i < (event.isShiftClick() ? 10 : 1) * multiplier; i++) {
                        UpgradeGuiPickaxe upgradeGuiPickaxe = (UpgradeGuiPickaxe) event.getClickedInventory().getHolder();
                        upgradeGuiPickaxe.onClick(event.getSlot());
                        if(!upgradeGuiPickaxe.canAfford())
                            break;
                    }
                }
                if(event.getClickedInventory().getHolder() instanceof UpgradeGui2Pickaxe) {
                    for (int i = 0; i < (event.isShiftClick() ? 10 : 1) * multiplier; i++) {
                        UpgradeGui2Pickaxe upgradeGui2Pickaxe = (UpgradeGui2Pickaxe) event.getClickedInventory().getHolder();
                        upgradeGui2Pickaxe.onClick(event.getSlot());
                        if(!upgradeGui2Pickaxe.canAfford())
                            break;
                    }
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
