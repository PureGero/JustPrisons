package net.justminecraft.prisons.customloot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LootListener implements Listener {

    private final LootManager lootManager;

    public LootListener(LootManager lootManager) {
        this.lootManager = lootManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

    }
}
