package net.justminecraft.prisons.spawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {

    private final SpawnManager spawnManager;

    public SpawnListener(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(spawnManager.getSpawn().getSpawnLocation().add(Math.random(), 0, Math.random()));

        e.getPlayer().setFoodLevel(20);
    }

}
