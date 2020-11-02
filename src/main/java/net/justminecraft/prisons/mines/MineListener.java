package net.justminecraft.prisons.mines;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class MineListener implements Listener {
    private final MineManager mineManager;

    public MineListener(MineManager mineManager) {
        this.mineManager = mineManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (mineManager.getMine(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Mine mine = mineManager.getMine(event.getPlayer());

        if (mine == null) {
            return;
        }

        if (!mine.inMine(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        event.setExpToDrop(0);
        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, event.getBlock().getType());
        event.getBlock().setType(Material.AIR);

        mine.onBlockBreak();
    }
}
