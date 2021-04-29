package net.justminecraft.prisons.plots;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlotListener implements Listener {
    private final PlotManager plotManager;

    public PlotListener(PlotManager plotManager) {
        this.plotManager = plotManager;
    }
    
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (plotManager.isPlotWorld(event.getClickedBlock().getWorld())
                && !plotManager.isInsideBuildArea(event.getClickedBlock())) {
            event.setCancelled(true);
        }
    }
}
