package net.justminecraft.prisons.spawn;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.World;

public class SpawnManager {

    private World world;

    public SpawnManager(PrisonsPlugin plugin) {
        world = plugin.getServer().getWorlds().get(0);

        plugin.getServer().getPluginManager().registerEvents(new SpawnListener(this), plugin);
    }

    public World getWorld() {
        return world;
    }
}
