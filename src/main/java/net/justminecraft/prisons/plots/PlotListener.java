package net.justminecraft.prisons.plots;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class PlotListener implements Listener {
    private final PlotManager plotManager;

    public PlotListener(PlotManager plotManager) {
        this.plotManager = plotManager;
    }
    
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && plotManager.isPlotWorld(event.getClickedBlock().getWorld())
                && !plotManager.isInsideBuildArea(event.getClickedBlock())
                && !plotManager.isInsideMine(event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        plotManager.removeMine(event.getWorld());
    }
}
